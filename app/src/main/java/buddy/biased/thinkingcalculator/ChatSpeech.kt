package buddy.biased.thinkingcalculator

data class ChatSpeech(val id: Long = -1, val weight: Int, val category: String, val trigger: String, val speech: String)