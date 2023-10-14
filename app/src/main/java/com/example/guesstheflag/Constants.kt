package com.example.guesstheflag

object Constants {

    fun getQuizz(): ArrayList<Question>{
        val questionsList = ArrayList<Question>()
        val flagOptions = FlagRandomizer.flagImages.toMutableList()

        for(index in 0 until 10){
            val ansImage = FlagRandomizer.getRandomFlag(flagOptions)
            flagOptions.remove(ansImage)
            val ansIndex = (0 until 4).random()

            val incorrectAnswerOne = FlagRandomizer.getRandomFlag(flagOptions)
            flagOptions.remove(incorrectAnswerOne)

            val incorrectAnswerTwo = FlagRandomizer.getRandomFlag(flagOptions)
            flagOptions.remove(incorrectAnswerTwo)

            val incorrectAnswerThree = FlagRandomizer.getRandomFlag(flagOptions)
            flagOptions.remove(incorrectAnswerThree)

            val localAnswers: Array<Pair<Int, String>> = arrayOf(
                incorrectAnswerOne,
                incorrectAnswerTwo,
                incorrectAnswerThree
            )

            val optionsList: ArrayList<Pair<Int, String>> = ArrayList()
            optionsList.add(Pair(0, ""))
            optionsList.add(Pair(0, ""))
            optionsList.add(Pair(0, ""))
            optionsList.add(Pair(0, ""))

            optionsList[ansIndex] = ansImage
            var localAnswersSize = localAnswers.size - 1
            for(ind in 0 until 4){
                if(ind != ansIndex){
                    optionsList[ind] = localAnswers[localAnswersSize --]
                }
            }
            questionsList.add(Question(
                ansIndex,
                optionsList,
                ansImage
            ))

            flagOptions.add(incorrectAnswerOne)
            flagOptions.add(incorrectAnswerTwo)
            flagOptions.add(incorrectAnswerThree)
        }
        return questionsList
    }
}