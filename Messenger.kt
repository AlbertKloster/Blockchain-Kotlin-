package blockchain

import java.util.Random

class Messenger(name: String, private val messageList: MutableList<String>) : Thread(name) {
    private val messages = listOf(
        "Hi, there!",
        "I'm fine! How are you?",
        "Hello, dude!",
        "What's up, guys!",
        "I'm new here!",
        "Hello, everyone!",
        "Nice to meet you again!",
        "What goes!",
        "Here we are!",
    )
    override fun run() {
        messageList.add("$name: ${messages[Random().nextInt(messages.size)]}")
    }
}