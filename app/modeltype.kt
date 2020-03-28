class modeltype : ArrayList<modeltypeItem>(){
    data class modeltypeItem(
        val id: String,
        val name: String,
        val type: String,
        val url: String
    )
}