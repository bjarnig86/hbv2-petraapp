package `is`.hi.hbv601g.petraapp.networking

interface NetworkCallback<T> {
    fun onSuccess(result: T)
    fun onFailure(errorString: String)
}