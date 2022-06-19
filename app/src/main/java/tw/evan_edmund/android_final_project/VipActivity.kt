package tw.evan_edmund.android_final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*

class VipActivity : AppCompatActivity() {
    lateinit var account_ET : EditText
    lateinit var password_ET: EditText
    lateinit var valid_ET: EditText
    lateinit var pay_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vip)
        account_ET = findViewById(R.id.account)
        password_ET = findViewById(R.id.verification_code)
        valid_ET = findViewById(R.id.valid_date)
        pay_btn = findViewById(R.id.pay_btn)
        pay_btn.setOnClickListener { checkAccount() }

    }
    private fun checkAccount(){
        val account = account_ET.getText().toString()
        val password = password_ET.getText().toString()
        val valid_date = valid_ET.getText().toString()

        var acc_correct = false
        var pass_correct = false
        var date_correct = false

        var c: Calendar = Calendar.getInstance()
        c.setTimeInMillis(System.currentTimeMillis())
        val current_month = c.get(Calendar.MONTH)
        val current_year = c.get(Calendar.YEAR)

//        Toast.makeText(this, "${current_month}, ${current_year}", Toast.LENGTH_SHORT).show()
        if(account == "12345678"){
            acc_correct = true
        }
        else{
            Toast.makeText(this, "Invalid Credit Card Number", Toast.LENGTH_SHORT).show()
        }
        if(password == "1234"){
            pass_correct = true
        }
        else{
            Toast.makeText(this, "Invalid Verification Code", Toast.LENGTH_SHORT).show()
        }
        val input = valid_date.split("/").toTypedArray()
        if(input.size != 2){
            Toast.makeText(this, "Wrong input format(Valid date)", Toast.LENGTH_SHORT).show()
        }
        else if(input[0].toInt()<0 || input[0].toInt()>12){
            Toast.makeText(this, "Invalid input(Valid date)", Toast.LENGTH_SHORT).show()
        }
        else if(input[1].toInt()<1000 || input[1].toInt()>9999){
            Toast.makeText(this, "Invalid input(Valid date)", Toast.LENGTH_SHORT).show()
        }
        else{
            if((current_month>input[0].toInt()&&current_year==input[1].toInt()) || current_year<input[1].toInt()){
                date_correct = true
            }
            else{
                Toast.makeText(this, "Expired Credit Card", Toast.LENGTH_SHORT).show()
            }
        }
        if(acc_correct && pass_correct && date_correct){
            Toast.makeText(this, "Congratulation! \nYou are VIP now", Toast.LENGTH_SHORT).show()
            MainActivity.VIP_check.is_vip = true
            intent.setClass(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }
}