import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("image_upload.php")
    fun sendImage(
        @Part imageFile: MultipartBody.Part
    ): Call<ImageUploadResponse>
}
data class ImageUploadResponse(val imageUrl: String)