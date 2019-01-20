package buddy.biased.thinkingcalculator

/**
 * Created by Michal on 5/27/2018.
 */
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import buddy.biased.thinkingcalculator.databinding.FragmentCalculatorBinding
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat
import kotlinx.android.synthetic.main.fragment_calculator.*


class CalculatorFragment : Fragment() {

    companion object {
        private var currentValue: String = "0"
        private var _backspacelist:MutableList<String> =  mutableListOf<String>("0")
        private var _backspacebracketlist:MutableList<Int> =  mutableListOf<Int>(0)
        private var bindcalModel = CalculatorModel()
        private var leftBracketCount = 0
        private var lastPressedButton: String = ""
        lateinit var dbHelper: SQLiteDatabaseHelper
        private val CHATVALUES = arrayOf("0", "1", "2", "3","5","DEL","CLEAR","EQUALS")

        fun newInstance(): CalculatorFragment {
            return CalculatorFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var binding : FragmentCalculatorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculator,container,false)
        var myView : View  = binding.root
      //  val view: View = inflater!!.inflate(R.layout.fragment_calculator, container, false)
        binding.calculatorModel = bindcalModel

        return myView
    //   return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        dbHelper = SQLiteDatabaseHelper(activity)

        buttonSquared.text = Html.fromHtml("X<sup>y</sup>")

        buttonpi.setOnClickListener {
            buttonPress("PI")
        }
        button0.setOnClickListener {
            buttonPress("0")
        }
        buttonpoint.setOnClickListener {
            buttonPress("POINT")
        }
        buttonequals.setOnClickListener {
            buttonPress("EQUALS")
        }

        button1.setOnClickListener {
            buttonPress("1")
        }
        button2.setOnClickListener {
            buttonPress("2")
        }
        button3.setOnClickListener {
            buttonPress("3")
        }
        buttonplus.setOnClickListener {
            buttonPress("PLUS")
        }

        button4.setOnClickListener {
            buttonPress("4")
        }
        button5.setOnClickListener {
            buttonPress("5")
        }
        button6.setOnClickListener {
            buttonPress("6")
        }
        buttonminus.setOnClickListener {
            buttonPress("MINUS")
        }

        button7.setOnClickListener {
            buttonPress("7")
        }
        button8.setOnClickListener {
            buttonPress("8")
        }
        button9.setOnClickListener {
            buttonPress("9")
        }
        buttonx.setOnClickListener {
            buttonPress("MULTIPLICATION")
        }

        buttonExp.setOnClickListener {
            buttonPress("EXP")
        }
        buttonC.setOnClickListener {
            buttonPress("CLEAR")
        }
        buttonDEL.setOnClickListener {
            buttonPress("DEL")
        }
        buttondivision.setOnClickListener {
            buttonPress("DIVISION")
        }

        buttonLeftBracket.setOnClickListener {
            buttonPress("LEFTBRACKET")
        }
        buttonRightBracket.setOnClickListener {
            buttonPress("RIGHTBRACKET")
        }
        buttonSquared.setOnClickListener {
            buttonPress("SQUARE")
        }
        buttonSquareRoot.setOnClickListener {
            buttonPress("ROOTSQUARE")
        }
        buttonSIN.setOnClickListener {
            buttonPress("SIN")
        }
        buttonCOS.setOnClickListener {
            buttonPress("COS")
        }

        buttonTAN.setOnClickListener {
            buttonPress("TAN")
        }
        buttonCOT.setOnClickListener {
            buttonPress("COT")
        }
        buttonLN.setOnClickListener {
            buttonPress("LN")
        }
        buttonLOG.setOnClickListener {
            buttonPress("LOG")
        }

        history_text_view.setOnClickListener {

          //  replaceFragment(HistoryFragment,"History")

        }

    }

    private fun buttonPress(pressedkey: String) {

          var buttonPressWorked: Boolean = java.lang.Boolean.TRUE
    //    val resultTextView = getView()?.findViewById<TextView>(R.id.result_text_view)
    //    val historyTextView = getView()?.findViewById<TextView>(R.id.history_text_view)

        if ((lastPressedButton == "EQUALS")&&(!isLastCharacterNumber())&&lastPressedButton != "ROOTSQUARE") currentValue =  "0"

        when (pressedkey) {

            "0","1","2","3","4","5","6","7","8","9" -> {
                if (isLastCharacterRightBracket() || isLastCharacterConstat()) currentValue = StringBuilder().append(currentValue).append("*").toString()
                currentValue = if (currentValue == "0" || lastPressedButton == "EQUALS") pressedkey else StringBuilder().append(currentValue).append(pressedkey).toString()
            }

            "PI" -> {
                updateFormulaForStringOperators("pi")
            }

            "POINT" -> {
                if (currentValue!="") {
                    var resultlenght = currentValue.length - 1
                    while (("0123456789".contains(currentValue.get(resultlenght))) && (resultlenght > 0)) {
                        resultlenght--
                    }

                    if ("+-".contains(currentValue.get(resultlenght)) && resultlenght>0){
                        if (currentValue.get(resultlenght-1) == 'E'){
                            buttonPressWorked = java.lang.Boolean.FALSE
                        }
                    }

                    if ((!"E.".contains(currentValue.get(resultlenght)))&&buttonPressWorked) {
                        if (!"0123456789".contains(currentValue.get(currentValue.length - 1))) {
                            if ("ie".contains(currentValue.get(currentValue.length - 1))) {
                                currentValue = StringBuilder().append(currentValue).append("*").toString()
                            }
                            currentValue = StringBuilder().append(currentValue).append("0").toString()
                        }
                        currentValue = StringBuilder().append(currentValue).append(".").toString()
                    }
                    else buttonPressWorked = java.lang.Boolean.FALSE
                }
                else if(lastPressedButton == "EQUALS" || currentValue=="") currentValue = "0."
            }

            "PLUS" -> {
                updateFormula("+")
            }

            "MINUS" -> {
                if (currentValue==""||currentValue=="0") {
                    currentValue = "-"
                }
                else if ('.'==((currentValue.get(currentValue.length - 1)))) {
                    currentValue = StringBuilder().append(currentValue).append("0-").toString()
                }
                else if(currentValue.get(currentValue.length - 1)!='-') {
                    currentValue = StringBuilder().append(currentValue).append("-").toString()
                }
            }

            "DIVISION" -> {
                updateFormula("/")
            }

            "MULTIPLICATION" -> {
                updateFormula("*")
            }

            "LEFTBRACKET" -> {
                var expression = ""
                if (currentValue=="0") {
                    currentValue = "("
                }
                else
                {if (isLastCharacterNumber()||isLastCharacterRightBracket() || isLastCharacterConstat()) expression = "*"
                    currentValue = StringBuilder().append(currentValue).append(expression+"(").toString()
                }
                leftBracketCount++
            }

            "RIGHTBRACKET" -> {
                if (leftBracketCount>0) {
                    currentValue = StringBuilder().append(currentValue).append(")").toString()
                    leftBracketCount--
                }
            }

            "SQUARE" -> {
                updateFormula("^")
            }

            "SIN" -> {
                updateFormulaForStringOperators("Sin(")
                leftBracketCount++
            }

            "COS" -> {
                updateFormulaForStringOperators("Cos(")
                leftBracketCount++
            }

            "TAN" -> {
                updateFormulaForStringOperators("Tan(")
                leftBracketCount++
            }

            "COT" -> {
                updateFormulaForStringOperators("Cot(")
                leftBracketCount++
            }

            "LN" -> {
                updateFormulaForStringOperators("ln(")
                leftBracketCount++
            }

            "LOG" -> {
                updateFormulaForStringOperators("log10(")
                leftBracketCount++
            }

            "ROOTSQUARE" -> {
                var expression = ""
                if (lastPressedButton=="EQUALS") {currentValue ="sqrt("+currentValue+")"
                    lastPressedButton = pressedkey
                    buttonPress("EQUALS")
                    buttonPressWorked = false
                }
                else if (currentValue=="0"){
                    currentValue = "sqrt("
                    leftBracketCount++
                }
                else
                {
                    if (isLastCharacterNumber()||isLastCharacterRightBracket()||isLastCharacterConstat()) expression = "*"

                    currentValue = StringBuilder().append(currentValue).append(expression+"sqrt(").toString()
                    leftBracketCount++
                }
            }

            "EXP" -> {
                currentValue = if (currentValue == "0" || lastPressedButton == "EQUALS") "e"
                else {
                    var expression = ""
                    if (isLastCharacterNumber() || isLastCharacterRightBracket() || isLastCharacterConstat()) expression = "*"
                    StringBuilder().append(currentValue).append(expression + "e").toString()
                }
            }

            "CLEAR" -> {
                currentValue = "0"
                leftBracketCount=0
            }

            "DEL" -> {
                if (_backspacelist.size>1) {
                    _backspacelist.removeAt(_backspacelist.size-1)
                    currentValue = _backspacelist.last()

                    _backspacebracketlist.removeAt(_backspacebracketlist.size-1)
                    leftBracketCount = _backspacebracketlist.last()
                }

            }

            "EQUALS" -> {
                if (!isResultViewEmpty()) {

                    currentValue = removeUnfinishedExpressions(currentValue)
                    while (leftBracketCount>0){
                        leftBracketCount--
                        currentValue = StringBuilder().append(currentValue).append(")").toString()
                    }
                    var formula = currentValue +" = "

                    try{
                     val result = ExpressionBuilder(currentValue).function(sindeg).function(cosdeg).function(tandeg).function(cotdeg).function(lnlog)
                            .build()

                        currentValue = formatDoubleNumber(result.evaluate())

                    }
                    catch(e:Exception){
                        currentValue="error"
                        (activity as MainActivity).robot_text_view.text = getChatMessage("errormsg")
                        (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robot)
                        (activity as MainActivity).timepassed=0
                    }

                    logEquation(formula, currentValue)
                    history_text_view.text = dbHelper.getLatestEquation()
               //     bindcalModel.latestHistory = dbHelper.getLatestEquation()
                    leftBracketCount=0
                }
            }

        }

        if(buttonPressWorked) lastPressedButton = pressedkey
        result_text_view.text = currentValue
        if((pressedkey!="DEL")&&(currentValue != _backspacelist.last())) {
            _backspacelist.add(currentValue)
            _backspacebracketlist.add(leftBracketCount)
        }

            if ((activity as MainActivity).timepassed>=3000) {

                if(!CHATVALUES.contains(pressedkey)){
                    (activity as MainActivity).robot_text_view.text = getChatMessage("random")
                    when((1..4).shuffled().first()) {
                        1 -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robotrandom)
                        2 -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robotrandom2)
                        3 -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robotrandom3)
                        4 -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robotrandom4)
                    }
                }
                else{
                    (activity as MainActivity).robot_text_view.text = getChatMessage(pressedkey)
                when(pressedkey){
                    "3" -> {
                        when((1..2).shuffled().first()) {
                            1 -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robot3)
                            2 -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robot3b)
                        }
                    }
                    "EQUALS" -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robotequals)
                    "CLEAR" -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robotclear)
                    "DEL" -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robotdel)
                    "1" -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robot1)
                    "2" -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robot2)
                    "5" -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robot5)
                    "0" -> (activity as MainActivity).robot_imageView.setImageResource(R.drawable.robot0)
                }
                }

                (activity as MainActivity).timepassed=0
            }
    }

    fun getChatMessage(category: String): String {
        return dbHelper.getRandomSpeechByCategory(category)
    }

    private fun updateFormula(pressedexpression: String){
        if (!isResultViewEmpty()) {
            if ('.'==((currentValue.get(currentValue.length - 1)))) {
                currentValue = StringBuilder().append(currentValue).append("0"+pressedexpression).toString()
            }
            else if (isLastCharacterNumber()||isLastCharacterRightBracket()||isLastCharacterConstat()) {
                if (currentValue != "0") {
                    currentValue = StringBuilder().append(currentValue).append(pressedexpression).toString()
                }
            }
        }
    }

    private fun updateFormulaForStringOperators(pressedexpression: String){
        currentValue = if (currentValue == "0" || lastPressedButton == "EQUALS") pressedexpression
        else
        {
            if(!isResultViewEmpty()) {
                if ("0123456789)ie".contains(currentValue.get(currentValue.length - 1))) {
                    currentValue = StringBuilder().append(currentValue).append("*").toString()
                }
            }
            StringBuilder().append(currentValue).append(pressedexpression).toString()

        }
    }

    private fun isResultViewEmpty():Boolean{
        if (currentValue=="") return java.lang.Boolean.TRUE
        else return java.lang.Boolean.FALSE
    }

    private fun isLastCharacterNumber():Boolean{
        if(!isResultViewEmpty()){
            if ("0123456789".contains(currentValue.get(currentValue.length - 1))){
                return java.lang.Boolean.TRUE
            }
            else return java.lang.Boolean.FALSE
        }
        else return java.lang.Boolean.FALSE
    }

    private fun isLastCharacterRightBracket():Boolean{
        if(!isResultViewEmpty()){
            if (")".contains(currentValue.get(currentValue.length - 1))){
                return java.lang.Boolean.TRUE
            }
            else return java.lang.Boolean.FALSE
        }
        else return java.lang.Boolean.FALSE
    }

    private fun isLastCharacterConstat():Boolean{
        if(!isResultViewEmpty()){
            if ("ie".contains(currentValue.get(currentValue.length - 1))){
                return java.lang.Boolean.TRUE
            }
            else return java.lang.Boolean.FALSE
        }
        else return java.lang.Boolean.FALSE
    }

    private fun removeUnfinishedExpressions(currentValue: String):String {
        var resultlenght = currentValue.length - 1

        while (("*/+-(.^".contains(currentValue.get(resultlenght))) && (resultlenght > 0)) {
            if(currentValue.get(resultlenght)=='('){
                leftBracketCount--
            }
            else if(currentValue.get(resultlenght)==')'){
                leftBracketCount++
            }
            resultlenght--
        }
        return if (resultlenght<0) "0" else currentValue.substring(0,resultlenght+1)
    }

    private fun formatDoubleNumber(result : Double): String{

        var formatedresult = DecimalFormat("#")
        var count =(formatedresult.format(result).toString()).length

        if ((count>12 || Math.abs(result) <0.0000000001)&&(result!=0.0))
            formatedresult = DecimalFormat("0.00####E0")
        else {
            val resultpattern:String = "#."+"##########".substring(minOf(count,9))
            formatedresult = DecimalFormat(resultpattern)
        }
        return(""+formatedresult.format(result))

    }

    fun logEquation(formula: String, formulaResult: String) {
        dbHelper.logEquation(formula, formulaResult)
    }

    var sindeg: net.objecthunter.exp4j.function.Function = object : net.objecthunter.exp4j.function.Function("Sin") {
        override fun apply(vararg args: Double): Double {
            return Math.sin(Math.toRadians(args[0]))
        }
    }

    var cosdeg: net.objecthunter.exp4j.function.Function = object : net.objecthunter.exp4j.function.Function("Cos") {
        override fun apply(vararg args: Double): Double {
            return Math.cos(Math.toRadians(args[0]))
        }
    }

    var tandeg: net.objecthunter.exp4j.function.Function = object : net.objecthunter.exp4j.function.Function("Tan") {
        override fun apply(vararg args: Double): Double {
            return Math.tan(Math.toRadians(args[0]))
        }
    }

    var cotdeg: net.objecthunter.exp4j.function.Function = object : net.objecthunter.exp4j.function.Function("Cot") {
        override fun apply(vararg args: Double): Double {
            val tan = Math.tan(Math.toRadians(args[0]))
            return if (tan == 0.0) {
                throw ArithmeticException("Division by zero in cotangent!")
            } else {
                1.0 / Math.tan(Math.toRadians(args[0]))
            }
        }
    }

    var lnlog: net.objecthunter.exp4j.function.Function = object : net.objecthunter.exp4j.function.Function("ln") {
        override fun apply(vararg args: Double): Double {
            return Math.log(args[0])
        }
    }

}
