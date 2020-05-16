package fr.epf.moov

data class MetroLine (val code: String, val name: String, val directions: String, val id: Int)
{
    companion object {
        val all = arrayOf<MetroLine>().toMutableList()
    }
}