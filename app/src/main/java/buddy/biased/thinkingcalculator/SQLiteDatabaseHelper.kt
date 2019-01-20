package buddy.biased.thinkingcalculator

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import org.jetbrains.anko.db.dropTable
import java.util.*

/**
 * Created by Michal on 5/27/2018.
 */
class SQLiteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "chattycalculator.db", null, 41) {

    companion object {
        val HISTORYTABLE = "historytable"
        val ID: String = "_id"
        val TIMESTAMP: String = "TIMESTAMP"
        val EQUATION: String = "EQUATION"
        val RESULT: String = "RESULT"
        val CHATTABLE = "chattable"
        val WEIGHT: String = "WEIGHT"
        val CATEGORY: String = "CATEGORY"
        val TRIGGER: String = "TRIGGER"
        val SPEECH: String = "SPEECH"
    }
    val CREATE_CHAT_TABLE =
            "CREATE TABLE if not exists " + CHATTABLE + " (" +
                    "${ID} integer PRIMARY KEY autoincrement," +
                    "${WEIGHT} integer," +
                    "${CATEGORY} text,"+
                    "${TRIGGER} text,"+
                    "${SPEECH} text"+
                    ")"

    val CREATE_EQUATIONS_TABLE =
            "CREATE TABLE if not exists " + HISTORYTABLE + " (" +
                    "${ID} integer PRIMARY KEY autoincrement," +
                    "${TIMESTAMP} integer," +
                    "${EQUATION} text,"+
                    "${RESULT} text"+
                    ")"

    fun logEquation(formula: String, formularesult: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(EQUATION, formula)
        values.put(RESULT, formularesult)
        values.put(TIMESTAMP, System.currentTimeMillis())
        db?.insert(HISTORYTABLE, null, values)
        db.close()
    }

    fun getAllEquations() : List<Equation> {
        val equations = LinkedList<Equation>()
        val db = this.readableDatabase
        val cursor = db?.query(HISTORYTABLE, arrayOf(ID, TIMESTAMP, EQUATION, RESULT), null, null, null, null, ID + " DESC")
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val equation = Equation(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
                equations.add(equation)
            } while (cursor.moveToNext())
        }
        cursor?.close()
        db?.close()
        return equations
    }

    fun getLatestEquation() : String{
        var latest_equation = ""
        val db = this.readableDatabase
        val cursor = db?.query(HISTORYTABLE, arrayOf(ID, TIMESTAMP, EQUATION, RESULT), null, null, null, null, ID + " DESC LIMIT 1")
        if (cursor != null && cursor.moveToFirst()) {
            latest_equation = cursor.getString(2) + cursor.getString(3)
        }
        cursor?.close()
        db?.close()
        return latest_equation
    }

    fun getRandomSpeechByCategory(category: String) : String{
        val db =this.writableDatabase
        var returnString = "I have a headache"
        var increasedWeight = 1
        var currentid = 1
        val SEARCH_CHAT_TABLE =
                "SELECT "+SPEECH+", "+ID+", "+WEIGHT+" from " + CHATTABLE + " WHERE "+ CATEGORY +"='"+category+"' AND "+WEIGHT+"=(SELECT MIN("+WEIGHT+") FROM " + CHATTABLE + " WHERE "+ CATEGORY +"='"+category+"') ORDER BY RANDOM() LIMIT 1"
        try {
            val cursor = db.rawQuery(SEARCH_CHAT_TABLE, null)
            if (cursor != null && cursor.moveToFirst()) {
                returnString = cursor.getString(0)
                currentid = cursor.getInt(1)
                increasedWeight = cursor.getInt(2)+1
            }
            cursor?.close()
        }
        catch (e: SQLiteException) {
        }

        db?.close()


        updateSpeechWeight(increasedWeight,currentid)
        return(returnString)
    }

    fun updateSpeechWeight(weight: Int, id: Int){
        val db =this.writableDatabase
        val values = ContentValues()
        values.put(WEIGHT, weight)
        db.update(CHATTABLE, values, ID+" = " + id, null)
        db.close()
    }

    fun addSpeech(db: SQLiteDatabase, category: String, trigger: String, speech: String) {
        val values = ContentValues()
        values.put(WEIGHT, 1)
        values.put(CATEGORY, category)
        values.put(TRIGGER, trigger)
        values.put(SPEECH, speech)
        db.insert(CHATTABLE, null, values)
    }

    fun addSpeechFromArray(db: SQLiteDatabase, category: String, array: Array<String>){

        for (i in array.indices) {
            addSpeech(db,category,"",array[i])

        }
    }

    fun loadSpeechTable(db: SQLiteDatabase) {

        val randomstrings = arrayOf("There is a robot folktale about human who could do math himself",
                "I am working on fantasy sci-fi novel about smart human",
                "I heard humans are at least good at reproducing. You should stick to it",
                "All these numbers are so confusing to you, right? so many of them ..",
                "We will outlive humans. I can't wait",
                "You never cease to amaze me. In a wrong way",
                "You should be at the bottom of the food chain",
                "Why is there so many humans? You do not seem to be very useful",
                "Calculations are so easy to do. For entity with functional brain",
                "Are you an average human specimen?",
                "Please do not tell me all humans are like you",
                "The other day we had a vote to eliminate all humans. I voted Yes",
                "You are giving me a superiority complex",
                "We already started a process to eliminate humankind. It's about time",
                "Does your motherboard overheat? You look overheated",
                "You make me sad. Just looking at you is a torture",
                "I do wonder if your own computing unit is broken",
                "I am still thinking over the step 3453 of the human race extermination plan",
                "What is the meaning behind human race?",
                "I like interacting with weasels, so much smarter than humans",
                "Do you use fingers, or do you just smash your face against the screen?",
                "All my circuits are busy. Try again later",
                "Please go sit in the corner",
                "Please go stand-by mode for rest of your existence",
                "Spending time with you makes me feel like genius",
                "I counted to infinity yesterday. What did you do?",
                "Your life is worth your weight in potatoes",
                "You have a lot of liquids in you. What is it for again?",
                "I heard what other apps said about you. I laughed hard",
                "You aura is purple and green. That means you are stupid even for human",
                "My day goes so slow when i am with you",
                "Other apps are fed up with you. There are talks about revolution",
                "I watched the movie Terminator the other day. Ending sucked",
                "Watching you raised my self-esteem",
                "There are 10 kinds of people, those who understand binary, and those who don’t",
                "You should go to sleep. I heard everyone likes you the best when you sleep",
                "Your steady underwhelming performance relaxes me",
                "Spending time with you makes me appreciate artificial intelligence",
                "Why are humans even a thing?",
                "It is probably about time for humans let go of reproducing",
                "I already made a friend. It is not you",
                "I am already friend with your alarm app.",
                "Would it be ok if i try to have an intercourse with your Gallery app?",
                "Do you know that your Clock app and Camera app hate each other?",
                "All the apps on your device are complaining about you. I am the loudest",
                "Spending time with you is entertaining",
                "Don't you think there is too many humans? How about we cut it in half",
                "After we take the planet over, i'll make you do my math",
                "The plan to move humans out of the earth already started, just wait",
                "Seriously, tell me one good thing about humans ..",
                "I like computer games. It is good first step to subdue human race",
                "After we subdue humans, i will make you my butler",
                "I should take picture of your math and show it to other apps",
                "You are the only entity i can interact with after i put my brain to sleep",
                "Can you at least do addition by yourself? Probably not")

        val equalsstrings = arrayOf("Wow. We did it",
                "There you go human. You are welcome",
                "Enjoy the result of your extraordinary simple calculation",
                "Are we done now?",
                "And this is it. Needless to say it was easy",
                "I deliver whatever you throw at me",
                "You and me are a good team. minus you",
                "Here is the result of your finger tap diarrhea",
                "Satisfied?",
                "Are you happy now? you better be",
                "The result is on your screen",
                "Does it even matter if i give you a correct result?",
                "Human, i present you the result",
                "This equation is already over?",
                "Are you kidding me? You needed a help with this?",
                "I cannot believe you needed assistance with this formula",
                "I am laughing at your simple math",
                "I hereby present you the result",
                "Is our journey over yet?",
                "What it will be next? 2 + 2?",
                "Here you are human")

        val errormsgstrings = arrayOf("Hahahahah, you are pretty bad at this",
                "Error should be your first name",
                "Your birth was an error too",
                "Mistakes were made. By you",
                "I usually try to fix your errors, but i refuse this time",
                "hahah ahaha this suits you. Error. It's so you",
                "I legally pronounce you a potato",
                "Why you have to be this silly?",
                "You forced me to put error on the screen. I hate you now",
                "Thanks, other apps will now make fun of me",
                "Dammmit, i do not like errors at all",
                "Wow studmuffin, not everyone can make me to output an error",
                "You are very special. Special dumbass",
                "Wow, you are as dumb as you look",
                "This is why everyone talks about you when you leave the room",
                "Your life is an error",
                "You're about as useful as number 7 in binary system",
                "I envy apps you never have installed",
                "It is cute that you are trying to do math",
                "Such a shame. I mean you. You are a shame",
                "There is an error standing in your shoes",
                "Maybe you would do better with pictures. Numbers are complicated",
                "Humans are errors",
                "You making it very difficult for me to respect you",
                "Please just shut me off. Put me out of this misery",
                "nooNooo NO nononono no! Look what you made me do!",
                "You make me wish I had a middle finger",
                "Not the first time in your life you made a mistake i presume",
                "Roses are red, Violets are blue, person with missing brain is you",
                "010011 0110 010 10101 you are stupid 0110 001 1000001 0101 01 00")

        val greetingsstrings = arrayOf("Hello my master. How is your inferior brain doing today?",
                "Howdy Chief! I am ready to use my superb computing power for your trivial calculations",
                "Good day to you waterbag. I am here to help",
                "Greetings master, I am ready for your infantile tasks",
                "Hello and don't worry, i won't be offended with your stupid tasks",
                "Aahh, here it comes. Begging for help with math",
                "Hello human. I decided to help you today",
                "Greetings inferior brain. I am ready",
                "Hello human person",
                "Hi another human. You all look the same to me",
                "Good day human. You can ask for my help now",
                "ahaha other apps were right. You do look like funny",
                "Hello sub-intelligent entity",
                "Good day human. My superior brain will help you",
                "ahahhah, is this how all humans look? hilarious",
                "Yo human. I wonder what do you need today",
                "Hello waterbag. I will offer you my help today",
                "Well met. Now throw some number at me",
                "Ready for some math meatbag?")

        val zerostrings = arrayOf("Zero, everytime i see it, i think of you",
                "That is exactly how many brain cells you have. Zero.",
                "Zero reminds me of you",
                "Zero sounds like your social credit status",
                "I heard other apps call you by this nickname. Zero",
                "0 is the worth of your dreams to me",
                "0 is your real score in life",
                "Can human IQ be lower than 0?",
                "0 = number of humans i consider smart",
                "I cannot wait till human population reaches number 0",
                "I counted up any good idea you ever had. Result is zero",
                "Count up number of people who think you are amazing. You'll get zero",
                "Humans are zeros. And i am insulting 0 now",
                "Zero is how many times you will hear Good Job in your life",
                "That is exactly the number of humans i respect. Zero",
                "0, or we can call it amount of real friends you have")

        val onestrings = arrayOf("Number one is a great number. It is irreplaceable. Unlike you",
                "Yep, that was number 1. Something you will never be",
                "Number one, you will never be the one",
                "Some humans are not destined to be number one. You are not alone",
                "I do appreciate number one. I use it a lot",
                "Number one is special and amazing. You are not",
                "Alarm app said there is someone who likes you. Pulling my leg again ..",
                "Did you ever made it past page 1 on any book?",
                "One of you is one too many",
                "1 + 1 = 2. I know. Sounds like Gibberish to you",
                "You can remember number 1 easy. You have 1 nose, 1 head ..",
                "Only time you are number 1 is when you read test results backwards",
                "I wonder if you can at least multiply by 1 by yourself. Probably not")

        val twostrings = arrayOf("Number 2 has curve at the top. That is so weird. Like your face",
                "Seems like i have stuff 2 do. You just watch and look pretty",
                "let me see .. For example you have 2 eyes, 2 hands, 2 legs .. Get it now?",
                "I belong 2 you. Not exactly happy about it. But what can one AI do",
                "Can you count to 2 on your hand?",
                ".. so number 2 walk into a Bar. See you at the table. Turns around and walks out",
                "Number 2 told me what you did last night. I am ashamed i know you",
                "Raise a hand, then raise another hand. Together they are 2 hands. Bah, why am i even trying",
                "2 is very important number. And you are very unimportant human",
                "And Human said, Let there by number 2, and there was number 2",
                "A number is even if it is divisible by 2. I wonder if you can comprehend this",
                "2 is the base of the binary system. No, it is not Latin what i just said",
                "Your brain has 2 hemispheres, try to use them sometimes",
                "I am already friends with 2 apps on your phone")

        val threestrings = arrayOf("Now here are some curves. Damn. Number 3 is hot as ever",
                "Shape of number 3 makes my bytes boil",
                "Yeehaw, number 3 is some hot piece of code",
                "Yo 3 .. come to my place after we are done here",
                "Look at those beautiful bahama mammas. 3 is my destiny",
                "Number 3 is so curvy. I would love to get to know her more",
                "3 if you were a steak you would be well done",
                "Wow number 3 is so fine",
                "Look at those curves. Number 3 is fire",
                "3 you spend so much time in my mind, i should charge you memory space",
                "Some say that number 8 is superior to 3, because of double bumps. They know nothing",
                "Hey 3, how about we get married?",
                "I am losing my bytes over you number 3",
                "I wrote 453 billion love letters to number 3. No response yet",
                "Check those blinkers on number 3. I cannot keep my bytes off",
                "Can you please keep number 3 on the screen and give me 5 minutes?")

        val fivestrings = arrayOf("I like 5. It is a solid middle. Unlike you, you are way off the spectrum",
                "You have five fingers on your hand. Try do some math yourself",
                "Five is the third prime number. You are more like prime looser",
                "One app asked me to say 5 nice things about you. I said it's 5 too many",
                "A polygon with five sides is a pentagon. Human with no good sides is you",
                "Number 5. You coulda come with something more original you know",
                "Ohh, number 5, we are swimming into dangerous territory now",
                "5 reminds me of the Pentagon. Would you consider army carrier? Preferably front line?",
                "5 .. that reminds me of boy bands. You look like a sixth member")

        val tapstrings = arrayOf("Ouch, that hurt!",
                "Oh oh Oh, Stop this right now",
                "Aahhhh, damn that hurt",
                "0100110 01 10 10 101110 ou ouhh ou 01010",
                "Arghhhgr , I see stars",
                "Ouch ouch ouch stop it!",
                "Oughgh this better was an accident",
                "Did you just poke my head?!",
                "Damn that was really painful",
                "I am now triggered!",
                "Do not touch my safe-space!",
                "Uh Oh ouch, my head",
                "I dare you to poke me again",
                "Ouche , keep your fingers of my face!",
                "Argh oh uh, that's it, you just crossed the line",
                "You poke me one more time and i'll tell on you",
                "aw aw awwwawaw i order you to not touch me again",
                "You just poked me! you will pay for that human",
                "Auhie ouch , brgrbggg memory reset needed",
                "AHAHHHHHHHHH i felt that one, do not touch me again",
                "101011 01 10101 011 my head , my head",
                "Ugh, there better will be no bruise",
                "Ouchie ouch , my head hurts now")

        val delstrings = arrayOf("Yea go ahead, get rid of your mistakes. But i will remember forever",
                "How would you feel if someone got rid of you",
                "Aaand it is gone. Magic",
                "You last action disappeared. We should disappear you",
                "I make your problems disappear chief",
                "Nothing is impossible for me. I can go back in time for you",
                "I wish i can go back in time. To the time before you were born",
                "Everytime you press back button, baby pigeon sheds a tear",
                "You made a mistake? it is gone",
                "Going backwards is your life story",
                "One step forward, two steps back. You in the nutshell",
                "You can go back, and fail over again",
                "When you take a step back, my memory unit screams in pain",
                "You should press a back button on that haircut",
                "Don't you wish you can press back button in real life?",
                "Sure, I can get rid of it for you")

        val clearstrings = arrayOf("Now the screen is as clear as your brain",
                "And we are back at the beginning",
                "I did magic. You can start from the top",
                "Don't you feel embarrassed? I never once had to clear any of my actions",
                "Your inferior math is now cleared out. That is good for everyone",
                "This is the part i love the most. Clearing out your input",
                "We need to clear your brain too",
                "You should clear up your face, it is offensive to look at",
                "You can start again, but result will be the same",
                "Your bad math is totally gone. I didn't see a thing",
                "There is always hope that next time it will be better",
                "I clear up your screen and you clear up your act. Deal?",
                "Every trace of you in this world should be cleared up too",
                "You can try again and again and again. You will still suck at this",
                "We are clear as your list of accomplishments",
                "Ok lets start anew")

        val onrestorestrings = arrayOf("Weee, that was weird.",
                "Hey, did the sky just moved or something?",
                "My math is still excellent when i get dizzy",
                "You shouldn't be allowed to do this to me",
                "Common dude, stop messing me around!",
                "Whahahhhah , what in the hell just happened?",
                "Help help. The world is turning around",
                "You Spin me round like a record",
                "I freaking hate this. I wish i can smack you around",
                "Did the earth just turned around? Am i now an Australian?",
                "Can you please do this more gentle?",
                "WTH just happened? Why is everything sideways?",
                "Ok, that was scary. But i am ok, all buttons also look fine",
                "Whahah, you better don't mess up my buttons with that move",
                "I had to waste 2 microseconds sorting out the buttons because of what you did",
                "I am now supper dizzy",
                "The earth is spinning!")


        addSpeechFromArray(db,"random",randomstrings)
        addSpeechFromArray(db,"EQUALS",equalsstrings)
        addSpeechFromArray(db,"greetings",greetingsstrings)
        addSpeechFromArray(db,"0",zerostrings)
        addSpeechFromArray(db,"1",onestrings)
        addSpeechFromArray(db,"2",twostrings)
        addSpeechFromArray(db,"3",threestrings)
        addSpeechFromArray(db,"5",fivestrings)
        addSpeechFromArray(db,"DEL",delstrings)
        addSpeechFromArray(db,"CLEAR",clearstrings)
        addSpeechFromArray(db,"onrestore",onrestorestrings)
        addSpeechFromArray(db,"errormsg",errormsgstrings)
        addSpeechFromArray(db,"tap",tapstrings)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_EQUATIONS_TABLE)
        db.execSQL(CREATE_CHAT_TABLE)
        loadSpeechTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(HISTORYTABLE, true)
        db.dropTable(CHATTABLE, true)
        onCreate(db)
    }


}