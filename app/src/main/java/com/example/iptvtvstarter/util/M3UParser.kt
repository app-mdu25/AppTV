package com.example.iptvtvstarter.util

data class Channel(val id: String, val name: String, val logo: String?, val url: String, val group: String?)

object M3UParser {
    fun parse(text: String): List<Channel> {
        val lines = text.lines()
        val channels = mutableListOf<Channel>()
        var name = ""
        var logo: String? = null
        var group: String? = null

        for (line in lines) {
            when {
                line.startsWith("#EXTINF") -> {
                    name = extractName(line)
                    logo = extractAttr(line, "tvg-logo")
                    group = extractAttr(line, "group-title")
                }
                line.isNotBlank() && !line.startsWith("#") -> {
                    val url = line.trim()
                    channels += Channel(id = url.hashCode().toString(),
                        name = if (name.isBlank()) url else name,
                        logo = logo, url = url, group = group)
                    name = ""; logo = null; group = null
                }
            }
        }
        return channels
    }

    private fun extractAttr(line: String, key: String): String? {
        val regex = Regex("$key=\"(.*?)\"")
        return regex.find(line)?.groupValues?.get(1)
    }

    private fun extractName(line: String): String {
        return line.substringAfter(",", "").trim()
    }
}
