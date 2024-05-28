package com.example.plant

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import org.json.JSONException

class Fragment_FreeBoard : Fragment() {
    private lateinit var WriteFreePostFragment: Fragment_WriteFreePost
    private lateinit var floatingActionMenu: FloatingActionMenu
    private lateinit var fab1: FloatingActionButton
    private lateinit var fab2: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var requestQueue: RequestQueue
    private var userEmail: String? = null
    private var board_type: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_free_board, container, false)

        // Retrieve arguments
        userEmail = arguments?.getString("userEmail")
        board_type = arguments?.getInt("board_type")

        // Set board name based on board_type
        val boardNameTextView: TextView = view.findViewById(R.id.boardname)
        when (board_type) {
            1 -> boardNameTextView.text = "자유게시판"
            2 -> boardNameTextView.text = "자랑게시판"
            3 -> boardNameTextView.text = "분양/나눔 게시판"
            4 -> boardNameTextView.text = "용품정보 게시판"
            5 -> boardNameTextView.text = "식물정보 게시판"
            6 -> boardNameTextView.text = "질문게시판"
        }

        // Floating Action Menu
        floatingActionMenu = view.findViewById(R.id.floatingActionMenu)
        fab1 = view.findViewById(R.id.fab1)
        fab2 = view.findViewById(R.id.fab2)

        // Set click listeners for each FAB
        fab1.setOnClickListener {
            getMyPosts()
            floatingActionMenu.close(true)
        }

        val bundle = Bundle().apply {
            putString("userEmail", userEmail)
            board_type?.let { putInt("board_type", it) }
            putString("task", "write")
        }
        WriteFreePostFragment = Fragment_WriteFreePost().apply {
            arguments = bundle
        }
        fab2.setOnClickListener {
            replaceFragment(WriteFreePostFragment)
        }

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(requireContext())
        getPostData()

        // Setup SearchView
        val post_searchView = view.findViewById<SearchView>(R.id.post_search_view)
        post_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (::postAdapter.isInitialized) {
                    postAdapter.filter.filter(newText)
                }
                return true
            }
        })

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getPostData() {
        val url = "http://192.168.233.22:80/post.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, "$url?board_type=$board_type", null,
            Response.Listener { response ->
                val postList = mutableListOf<Post>()

                for (i in 0 until response.length()) {
                    try {
                        val postObject = response.getJSONObject(i)
                        val post = Post(
                            postObject.getInt("post_num"),
                            postObject.getInt("board_type"),
                            postObject.getString("post_title"),
                            postObject.getString("post_content"),
                            postObject.getString("post_writer"),
                            postObject.getString("post_date"),
                        )
                        postList.add(post)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                postAdapter = PostAdapter(postList) { post ->
                    val fragment = Fragment_Viewpost().apply {
                        arguments = Bundle().apply {
                            putString("post_num", post.post_num.toString())
                            putString("board_type", post.board_type.toString())
                            putString("post_title", post.post_title)
                            putString("post_content", post.post_content)
                            putString("post_writer", post.post_writer)
                            putString("post_date", post.post_date)
                            putString("userEmail", userEmail)
                            putBoolean("is_equal", userEmail == post.post_writer)
                        }
                    }
                    replaceFragment(fragment)
                }
                recyclerView.adapter = postAdapter
                postAdapter.filter.filter("")
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private fun getMyPosts() {
        val url = "http://192.168.233.22:80/mypost.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, "$url?board_type=$board_type&user_email=$userEmail", null,
            Response.Listener { response ->
                val postList = mutableListOf<Post>()

                for (i in 0 until response.length()) {
                    try {
                        val postObject = response.getJSONObject(i)
                        val post = Post(
                            postObject.getInt("post_num"),
                            postObject.getInt("board_type"),
                            postObject.getString("post_title"),
                            postObject.getString("post_content"),
                            postObject.getString("post_writer"),
                            postObject.getString("post_date"),
                        )
                        postList.add(post)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                if (postList.isEmpty()) {
                    showNoPostsDialog()
                } else {
                    postAdapter = PostAdapter(postList) { post ->
                        val fragment = Fragment_Viewpost().apply {
                            arguments = Bundle().apply {
                                putString("post_num", post.post_num.toString())
                                putString("board_type", post.board_type.toString())
                                putString("post_title", post.post_title)
                                putString("post_content", post.post_content)
                                putString("post_writer", post.post_writer)
                                putString("post_date", post.post_date)
                                putString("userEmail", userEmail)
                                putBoolean("is_equal", userEmail == post.post_writer)
                            }
                        }
                        replaceFragment(fragment)
                    }
                    recyclerView.adapter = postAdapter
                    postAdapter.filter.filter("")
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private fun showNoPostsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("게시물 없음")
        builder.setMessage("작성한 게시물이 없습니다!")
        builder.setPositiveButton("확인") { dialog, which ->
            // Do nothing
        }
        builder.show()
    }
}