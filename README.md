# Spirit 📖✨

**Spirit** es un mod para Minecraft 1.21.1 desarrollado con **NeoForge** que incorpora la **Santa Biblia completa en español** (Reina-Valera) directamente dentro del juego con una interfaz de lectura interactiva, inmersiva y moderna.

---

## Características

- 📜 **Biblia Completa en Español:** Contiene los 66 libros del canon bíblico (Antiguo y Nuevo Testamento), con más de 1.180 capítulos y más de 31.000 versículos.
- 🎨 **Lector de Libros Personalizado (`Screen`):**
  - Fondo oscuro con contraste elegante y texto nítido en alta resolución.
  - Títulos destacados en dorado y numeración clara de versículos.
  - Ajuste de renglones dinámico (*Word-wrapping*) para lectura cómoda.
- 🧭 **Navegación Avanzada:**
  - **Barra lateral interactiva de libros:** Acceso rápido a los 66 libros mediante abreviaturas estándar en español (`Gén`, `Éxo`, `Jn`, `Rom`, `Apo`, etc.).
  - **Tooltips dinámicos:** Al pasar el ratón sobre cualquier abreviatura, se muestra el nombre completo del libro.
  - **Desplazamiento dual con la rueda del ratón (*Smooth Scroll*):**
    - Rueda sobre la barra lateral: Desplaza la lista de libros.
    - Rueda sobre el texto: Desplaza los versículos del capítulo.
  - **Botones `<` y `>`:** Cambio ágil de capítulos con transición automática entre libros.
- ⚡ **Optimización de Memoria:**
  - Carga única en memoria RAM al inicio del juego mediante estructuras de datos eficientes en Java 21 (`record`, `ArrayList`).
  - Limpieza ortográfica y corrección tipográfica automática al inicio.

---

## Requisitos y Compatibilidad

- **Minecraft:** 1.21.1
- **Modloader:** NeoForge 21.1.250+
- **Java:** 21

---

## Compilación y Desarrollo

Para compilar el mod en tu entorno local:

```bash
# Compilar el código Java
./gradlew compileJava

# Probar en el cliente de desarrollo
./gradlew runClient

# Generar el archivo .jar instalable
./gradlew build
```

El archivo `.jar` resultante se generará en la carpeta `build/libs/`.

---

## Licencia

Distribuido bajo la licencia MIT / Open Source.
