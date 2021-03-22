package com.jjim4963tw.kotlinapp

class ExpressionsUtil {
    val colorCode: Char = 'R'

    fun ifElseNotReturnFunction() {
        var colorName: String? = null

        if (colorCode == 'R')
            colorName = "Red"
        else if (colorCode == 'B')
            colorName = "Blue"
        else if (colorCode == 'G')
            colorName = "Green"
        else
            colorName = "undefined"

        // colorName = "Red"
        print(colorName)
    }

    fun ifElseReturnFunction(): String {
        val colorName: String =
            if (colorCode == 'R')
                "Red"
            else if (colorCode == 'B')
                "Blue"
            else if (colorCode == 'G')
                "Green"
            else
                "undefined"

        // colorName = "Red"
        return colorName
    }

    fun javaSwitchIsKotlinWhen() {
        var colorName: String? = null

        when (colorCode) {
            'R','r' -> colorName = "Red"
            'B','b' -> colorName = "Blue"
            'G','g' -> colorName = "Green"
            else -> colorName = "undefined"
        }

        when {
            colorCode == 'R' -> colorName = "Red"
            colorCode == 'B' -> colorName = "Blue"
            colorCode == 'G' -> colorName = "Green"
            else -> colorName = "undefined"
        }

        // colorName = "Red"
    }

    fun whileAndDoWhile() {
        var i = 1
        var total = 0
        while (i <= 10) {
            total += i
            i++
        }

        // total = 10
        print(total)

        total = 0
        i = 1
        do {
            total += i
            i++
        }while (i < 10)

        // total = 9
        print("Total $total")
    }

    fun forLoop() {
        //print BCD
        for (letter in 'A'..'F') {
            if (letter == 'A') continue
            else if (letter == 'E') break
            else print(letter)
        }

        // print 123123123123123
        outer@ for (x in 1..5) {
            for (y in 1..5) {
                if (y > 3) {
                    continue@outer
                }

                print(y)
            }

            println("=>X:$x")
        }

        //print Text = 0, 1, 2, 3, 4
        for (i in 0..4) {
            if (i == 0) print("Text = $i")
            else        print(", $i")
        }

        //print Text = 1, 3, 5
        for (i in 1..5 step 2) {
            if (i == 0) print("Text = $i")
            else        print(", $i")
        }

        //print Text = 1, 2, 3, 4
        for (i in 1 until 5) {
            if (i == 0) print("Text = $i")
            else        print(", $i")
        }

        //print Text = 5, 4, 3, 2, 1
        for (i in 5 downTo 1) {
            if (i == 0) print("Text = $i")
            else        print(", $i")
        }

        //print Text = 5, 3, 1
        for (i in 5 downTo 1 step 2) {
            if (i == 0) print("Text = $i")
            else        print(", $i")
        }
    }
}