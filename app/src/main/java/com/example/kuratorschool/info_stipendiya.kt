package com.example.kuratorschool

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView // Импортируйте TextView

class info_stipendiya : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_info_stipendiya)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Находим TextView по ID
        val lectureContent: TextView = findViewById(R.id.lectureContent)

        // Текст о стипендиях
        val stipendiyaText = """
            Академическая стипендия:
                - 100% - 800 рублей
                - 125% - 1000 рублей
                - 150% - 1200 рублей

            Условия для получения:
                - 150% - все оценки "отлично";
                - 125% - есть и "отлично", и "хорошо";
                - 100% - все оценки "хорошо".

            Другие виды стипендий:
                - Социальная стипендия - 1200 рублей.
                - Стипендия старосты - 200 рублей.

            Когда приходит стипендия:
                - Стипендия приходит каждый месяц 25 числа. Если выпадает на выходные, приходит раньше.

            Периоды назначения академической стипендии:
                - с 1 сентября по 31 января - по результатам сессии за весенний семестр;
                - с 1 февраля по 30 июня - по результатам сессии за осенний семестр;
                - с 1 июля по 31 августа - летняя стипендия, назначается по результатам весеннего семестра.

            ❓ 125% независимо от того, сколько четвёрок?
                - Да, независимо. Иными словами, если вы сдаете пять предметов, четыре из них на «отлично» и один предмет «хорошо», вам будет назначено 125%.

            ❓ Как назначается стипендия первому курсу?
                - Стипендия на первом семестре назначается базовая, то есть у всех 100%.
                - Стипендия на второй семестр для студентов первого курса назначается по результатам осенней сессии.

            ❓ А стипендия для старост только со 2-го курса или со 2-го семестра? (для первого курса)
                - Надбавка за старосту назначается со второго семестра, по результатам сессии. Если вам не назначается обычная стипендия, то и надбавку вы не получаете.

            Ещё важный момент:
                - Даже если вы не получаете академическую стипендию (за учебу), вы имеете право получать социальную стипендию и писать заявление на материальную помощь.

            ‼ И, максимально важный момент:
                - Если вы должны получать стипендию, но не получили, обращайтесь с этим вопросом сразу же, а не спустя пару месяцев, чтобы найти решение проблемы быстрее.
        """.trimIndent() // Убираем лишние отступы

        // Устанавливаем текст в TextView
        lectureContent.text = stipendiyaText
    }
}