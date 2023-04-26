package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var canAddOperation= false
    private var canAddDecimal= true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun allClearAction(view: View) {
        binding.workingTV.text=""
        binding.resultsTV.text=""
    }
    fun numberAction(view: View) {
        if (view is Button){
            if(view.text=="."){
                if(canAddDecimal)
                   binding.workingTV.append(view.text)
                canAddOperation=false
            }
            else
               binding.workingTV.append(view.text)
            canAddOperation=true
        }
    }
    fun operationAction(view: View) {
        if (view is Button && canAddOperation){
            binding.workingTV.append(view.text)
            canAddOperation=false
            canAddDecimal=true
        }
    }
    fun backSpaceAction(view: View) {
        val length= binding.workingTV.length()
        if(length>0)
            binding.workingTV.text=binding.workingTV.text.subSequence(0,length-1)
    }
    fun equalsAction(view: View) {
        binding.resultsTV.text=calculateResult()
    }

    private fun calculateResult(): String{
        val digitOperators= digitsOperators()
        if(digitOperators.isEmpty())
            return ""

        val timeDivisions=timeDivisionCalculate(digitOperators)
        if(timeDivisions.isEmpty())
            return ""

        val result=addSubtractCalculate(timeDivisions)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Any {
            var result= passedList[0] as Float
            for(i in passedList.indices){
                if(passedList[i] is Char && i!=passedList.lastIndex){
                    val operator=passedList[i]
                    val nextDigit= passedList[i+1] as Float
                    if(operator== '+')
                        result+=nextDigit
                    if(operator== '-')
                        result-=nextDigit
                }
            }
            return result
    }

    private fun timeDivisionCalculate(passedList:MutableList<Any>): MutableList<Any> {
        var list = passedList
        while(list.contains('x') || list.contains('/')){
              list= calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
            val newList= mutableListOf<Any>()
            var restartIndex= passedList.size

            for(i in passedList.indices){
                if(passedList[i] is Char && i != passedList.lastIndex && i <restartIndex){
                    val operator= passedList[i]
                    val prevDigit= passedList[i-1] as Float
                    val nextDigit= passedList[i+1] as Float
                    when(operator){
                        'x'-> {
                            newList.add(prevDigit * nextDigit)
                            restartIndex=i+1
                        }
                        '/'-> {
                            newList.add(prevDigit / nextDigit)
                            restartIndex= i+1
                        }
                        else->{
                            newList.add(prevDigit)
                            newList.add(operator)
                        }
                    }
                }
                if(i>restartIndex)
                    newList.add(passedList[i])
            }

            return newList
    }

    private fun digitsOperators(): MutableList<Any>{
        val list= mutableListOf<Any>()
        var currentDigit=""
        for(character in binding.workingTV.text){
            if(character.isDigit() || character== '.' )
                currentDigit +=character
            else{
                list.add(currentDigit.toFloat())
                currentDigit=""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }

}