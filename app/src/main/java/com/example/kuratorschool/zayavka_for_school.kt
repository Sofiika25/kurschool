package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kuratorschool.Models.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import android.text.Editable
import android.text.TextWatcher

class PhoneNumberTextWatcher(private val editText: EditText) : TextWatcher {
    private var isFormatting = false
    private var deletingHyphen = false
    private var hyphenStart = 0
    private var deletingBackward = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (count > 0 && after == 0) {
            deletingBackward = (s?.getOrNull(start) == '-')
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Ничего не делаем
    }

    override fun afterTextChanged(s: Editable?) {
        if (isFormatting) {
            return
        }

        isFormatting = true

        val digits = s.toString().replace(Regex("[^\\d]"), "")
        val length = digits.length
        val formatted = StringBuilder()

        if (length >= 1) {
            formatted.append("+7 (")
            if (length > 1) {
                formatted.append(digits.substring(1, Math.min(4, length)))
            }
            if (length > 4) {
                formatted.append(") ").append(digits.substring(4, Math.min(7, length)))
            }
            if (length > 7) {
                formatted.append("-").append(digits.substring(7, Math.min(9, length)))
            }
            if (length > 9) {
                formatted.append("-").append(digits.substring(9, Math.min(11, length)))
            }
        }

        editText.removeTextChangedListener(this)
        editText.setText(formatted.toString())
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)

        isFormatting = false
    }
}
class zayavka_for_school : AppCompatActivity() {
    private val TAG = "ZayavkaForSchool"
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    private val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


    private val MAX_FULLNAME_LENGTH = 100
    private val MAX_GROUPNUMBER_LENGTH = 10
    private val MAX_PHONE_LENGTH = 25
    private val MIN_PHONE_LENGTH = 10
    private val MAX_VKLINK_LENGTH = 200
    private val MAX_TELEGRAM_LENGTH = 200
    private val MAX_CURATOR_REASON_LENGTH = 500
    private val MAX_POSITIVE_QUALITIES_LENGTH = 500
    private val MAX_NEGATIVE_QUALITIES_LENGTH = 500
    private val MAX_STUDENT_EXPERIENCE_LENGTH = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_zayavka_for_school)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val btnBack: TextView = findViewById(R.id.btn_back)
        val etFullName = findViewById<EditText>(R.id.usernameEditText)
        val etGroupNumber = findViewById<EditText>(R.id.passwordEditText)
        val etPhone = findViewById<EditText>(R.id.jh)
        etPhone.addTextChangedListener(PhoneNumberTextWatcher(etPhone))
        val etVkLink = findViewById<EditText>(R.id.pa)
        val etTelegram = findViewById<EditText>(R.id.u)
        val etCuratorReason = findViewById<EditText>(R.id.p)
        val etPositiveQualities = findViewById<EditText>(R.id.pp)
        val etNegativeQualities = findViewById<EditText>(R.id.ppp)
        val etStudentExperience = findViewById<EditText>(R.id.pppq)
        val btnSubmit: Button = findViewById(R.id.loginButton)


        btnBack.setOnClickListener {
            startActivity(Intent(this, school_info::class.java))
            finish()
        }


        if (currentUserId != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val fullNameFromDB = snapshot.child("full_name").getValue(String::class.java)
                    val groupIdFromDB = snapshot.child("group_id").getValue(Int::class.java)
                    if (!fullNameFromDB.isNullOrEmpty()) {
                        etFullName.setText(fullNameFromDB)
                        etFullName.isEnabled = false
                    }
                    if (groupIdFromDB != null) {
                        etGroupNumber.setText(groupIdFromDB.toString())
                        etGroupNumber.isEnabled = false
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@zayavka_for_school, "Ошибка загрузки данных пользователя", Toast.LENGTH_SHORT).show()
                }
            })
        }


        btnSubmit.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val groupNumber = etGroupNumber.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val vkLink = etVkLink.text.toString().trim()
            val telegram = etTelegram.text.toString().trim()
            val curatorReason = etCuratorReason.text.toString().trim()
            val positiveQualities = etPositiveQualities.text.toString().trim()
            val negativeQualities = etNegativeQualities.text.toString().trim()
            val studentExperience = etStudentExperience.text.toString().trim()


            if (fullName.isEmpty()) {
                etFullName.error = "Введите ФИО"
                etFullName.requestFocus()
                return@setOnClickListener
            }
            if (groupNumber.isEmpty()) {
                etGroupNumber.error = "Введите номер группы"
                etGroupNumber.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                etPhone.error = "Введите номер телефона"
                etPhone.requestFocus()
                return@setOnClickListener
            }
            if (vkLink.isEmpty()) {
                etVkLink.error = "Введите ссылку VK"
                etVkLink.requestFocus()
                return@setOnClickListener
            }
            if (telegram.isEmpty()) {
                etTelegram.error = "Введите аккаунт Telegram"
                etTelegram.requestFocus()
                return@setOnClickListener
            }
            if (curatorReason.isEmpty()) {
                etCuratorReason.error = "Введите причину"
                etCuratorReason.requestFocus()
                return@setOnClickListener
            }
            if (positiveQualities.isEmpty()) {
                etPositiveQualities.error = "Введите положительные качества"
                etPositiveQualities.requestFocus()
                return@setOnClickListener
            }
            if (negativeQualities.isEmpty()) {
                etNegativeQualities.error = "Введите негативные качества"
                etNegativeQualities.requestFocus()
                return@setOnClickListener
            }
            if (studentExperience.isEmpty()) {
                etStudentExperience.error = "Введите опыт в студенчестве"
                etStudentExperience.requestFocus()
                return@setOnClickListener
            }


            if (fullName.length > MAX_FULLNAME_LENGTH) {
                etFullName.error = "ФИО не может превышать $MAX_FULLNAME_LENGTH символов"
                etFullName.requestFocus()
                return@setOnClickListener
            }
            if (groupNumber.length > MAX_GROUPNUMBER_LENGTH) {
                etGroupNumber.error = "Номер группы не может превышать $MAX_GROUPNUMBER_LENGTH символов"
                etGroupNumber.requestFocus()
                return@setOnClickListener
            }
            if (phone.length < MIN_PHONE_LENGTH) {
                etPhone.error = "Номер телефона слишком короткий"
                etPhone.requestFocus()
                return@setOnClickListener
            }
            if (phone.length > MAX_PHONE_LENGTH) {
                etPhone.error = "Номер телефона не может превышать $MAX_PHONE_LENGTH символов"
                etPhone.requestFocus()
                return@setOnClickListener
            }
            if (vkLink.length > MAX_VKLINK_LENGTH) {
                etVkLink.error = "Ссылка VK не может превышать $MAX_VKLINK_LENGTH символов"
                etVkLink.requestFocus()
                return@setOnClickListener
            }
            if (telegram.length > MAX_TELEGRAM_LENGTH) {
                etTelegram.error = "Аккаунт Telegram не может превышать $MAX_TELEGRAM_LENGTH символов"
                etTelegram.requestFocus()
                return@setOnClickListener
            }
            if (curatorReason.length > MAX_CURATOR_REASON_LENGTH) {
                etCuratorReason.error = "Ответ не может превышать $MAX_CURATOR_REASON_LENGTH символов"
                etCuratorReason.requestFocus()
                return@setOnClickListener
            }
            if (positiveQualities.length > MAX_POSITIVE_QUALITIES_LENGTH) {
                etPositiveQualities.error = "Ответ не может превышать $MAX_POSITIVE_QUALITIES_LENGTH символов"
                etPositiveQualities.requestFocus()
                return@setOnClickListener
            }
            if (negativeQualities.length > MAX_NEGATIVE_QUALITIES_LENGTH) {
                etNegativeQualities.error = "Ответ не может превышать $MAX_NEGATIVE_QUALITIES_LENGTH символов"
                etNegativeQualities.requestFocus()
                return@setOnClickListener
            }
            if (studentExperience.length > MAX_STUDENT_EXPERIENCE_LENGTH) {
                etStudentExperience.error = "Ответ не может превышать $MAX_STUDENT_EXPERIENCE_LENGTH символов"
                etStudentExperience.requestFocus()
                return@setOnClickListener
            }





            val application = Application(
                fullName = fullName,
                groupNumber = groupNumber,
                phone = phone,
                vkLink = vkLink,
                telegram = telegram,
                curatorReason = curatorReason,
                positiveQualities = positiveQualities,
                negativeQualities = negativeQualities,
                studentExperience = studentExperience,
                status = "на рассмотрении",
                submissionDate = currentDate,
                user_id = currentUserId
            )


            val dbRef = FirebaseDatabase.getInstance().getReference("Applications")
            val newApplicationRef = dbRef.push()
            newApplicationRef.setValue(application).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Заявка отправлена. Статус: ${application.status}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Заявка успешно сохранена с ключом: ${newApplicationRef.key}")
                    startActivity(Intent(this, school_info::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Ошибка отправки заявки: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Ошибка при сохранении заявки", task.exception)
                }
            }
        }
    }
}
