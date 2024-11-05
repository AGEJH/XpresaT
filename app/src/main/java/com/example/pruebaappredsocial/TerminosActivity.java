package com.example.pruebaappredsocial;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TerminosActivity extends AppCompatActivity {
    private boolean hasScrolledToEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos);

        // Referencia al botón de regreso y al ScrollView
        ImageButton btnBack = findViewById(R.id.btnBack);
        ScrollView scrollView = findViewById(R.id.scrollView);
        // Configurar el contenido de los términos
        TextView tvTerminosContenido = findViewById(R.id.tv_terminos_contenido);
        tvTerminosContenido.setText("1). Propósito de la Aplicación:\n" +
                "Xpresat es una aplicación diseñada con fines universitarios, creada exclusivamente para contribuir al desarrollo educativo y el bienestar mental de los estudiantes. Su propósito es proporcionar un espacio seguro y anónimo donde los usuarios puedan expresar sus pensamientos, emociones y preocupaciones, con un enfoque especial en la prevención de ideaciones suicidas y otros comportamientos de riesgo. La aplicación está pensada para facilitar el monitoreo del bienestar estudiantil, identificar necesidades emocionales y proporcionar orientación de apoyo en temas de salud mental.\n" +
                "\n" +
                "2). Recopilación de Información Personal:\n" +
                "Al registrarse en Xpresat, los usuarios proporcionan ciertos datos personales básicos, como nombre, apellido, y correo electrónico institucional. Esta información es recopilada con el único propósito de brindar una experiencia segura, personalizada y en cumplimiento con las políticas de la universidad. Toda la información recopilada será tratada con la máxima confidencialidad y no será compartida con terceros sin el consentimiento explícito del usuario, a menos que sea necesario por motivos legales o de emergencia en el contexto de prevención de crisis.\n" +
                "\n" +
                "3). Contenido Generado por el Usuario:\n" +
                "Todo el contenido compartido dentro de la app, incluidas publicaciones, mensajes y comentarios, se recopila con fines educativos y de prevención. La universidad se reserva el derecho de revisar dicho contenido con el objetivo de detectar patrones de riesgo emocional o ideaciones suicidas. Este monitoreo se realiza exclusivamente para identificar posibles necesidades de intervención, garantizar un entorno seguro y brindar apoyo adecuado. Ninguna información será utilizada para otros fines, y el anonimato del usuario será prioritario en todos los casos.\n" +
                "\n" +
                "4). Política de Privacidad y Confidencialidad:\n" +
                "La Universidad de Colima se compromete a respetar y proteger la privacidad de los usuarios de Xpresat. La información personal y el contenido expresado en la app están protegidos bajo una política de confidencialidad. En caso de detectar una situación que represente un riesgo inminente para la seguridad del usuario o de terceros, la universidad podrá contactar con los servicios de emergencia o los recursos de apoyo correspondientes. Cualquier intervención será gestionada con respeto y sensibilidad hacia el usuario.\n" +
                "\n" +
                "5). Uso Responsable de Xpresat:\n" +
                "Al utilizar Xpresat, el usuario se compromete a:\n" +
                "Hacer un uso respetuoso de la aplicación y abstenerse de compartir contenido ofensivo, dañino o violento.\n" +
                "No difundir información personal o confidencial de otros usuarios.\n" +
                "Utilizar la app para expresar inquietudes y problemas de manera auténtica, especialmente si busca apoyo emocional.\n" +
                "Xpresat no es un sustituto de la atención profesional de salud mental. En caso de necesitar ayuda inmediata, se recomienda acudir a un profesional o servicio de emergencia.\n" +
                "\n" +
                "6). Limitación de Responsabilidad:\n" +
                "Xpresat y sus creadores no se hacen responsables por las decisiones o acciones que el usuario pueda tomar con base en la información o el contenido disponible en la aplicación. La app proporciona una plataforma de expresión y apoyo, pero no sustituye la asesoría profesional de psicólogos, consejeros o médicos.\n" +
                "\n" +
                "7). Modificaciones de los Términos y Condiciones:\n" +
                "La universidad se reserva el derecho de actualizar los términos y condiciones de uso de Xpresat conforme sea necesario para mantener la seguridad y el propósito de la aplicación. En caso de realizar modificaciones, se notificará a los usuarios a través de la app. Se recomienda que los usuarios revisen periódicamente estos términos.\n" +
                "\n" +
                "8). Contacto:\n" +
                "Para cualquier duda o inquietud sobre estos términos y condiciones, o si un usuario requiere asistencia, la Universidad de Colima pone a disposición su área de bienestar estudiantil y servicios de apoyo a través de los contactos establecidos en la app."
        );
        // Justificación del texto para API 26 en adelante
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvTerminosContenido.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
        // Escuchar eventos de desplazamiento en el ScrollView
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            // Verificar si el usuario ha llegado al final del ScrollView
            View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (lastChild.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // Si diff es 0 o menos, estamos al final
            if (diff <= 0) {
                hasScrolledToEnd = true;
            }
        });

        // Configurar el botón de regreso
        btnBack.setOnClickListener(view -> {
            if (hasScrolledToEnd) {
                // El usuario ha leído hasta el final; permitir el regreso
                Intent intent = new Intent(TerminosActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Mantener los datos de la actividad anterior
                startActivity(intent);
                finish();
            } else {
                // Mostrar mensaje si no ha leído hasta el final
                Toast.makeText(TerminosActivity.this, "Por favor, lea completamente los términos y condiciones de uso", Toast.LENGTH_SHORT).show();
            }
        });
    }
}