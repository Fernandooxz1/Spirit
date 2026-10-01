package com.fernando.modding;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BibleData {

    public record Book(String name, String abbrev, List<List<String>> chapters) {}

    public static final List<Book> BOOKS = new ArrayList<>();

    public static final String[] SPANISH_NAMES = {
            "Génesis", "Éxodo", "Levítico", "Números", "Deuteronomio",
            "Josué", "Jueces", "Rut", "1 Samuel", "2 Samuel",
            "1 Reyes", "2 Reyes", "1 Crónicas", "2 Crónicas", "Esdras",
            "Nehemías", "Ester", "Job", "Salmos", "Proverbios",
            "Eclesiastés", "Cantares", "Isaías", "Jeremías", "Lamentaciones",
            "Ezequiel", "Daniel", "Oseas", "Joel", "Amós",
            "Abdías", "Jonás", "Miqueas", "Nahúm", "Habacuc",
            "Sofonías", "Hageo", "Zacarías", "Malaquías",
            // Nuevo Testamento
            "Mateo", "Marcos", "Lucas", "Juan", "Hechos",
            "Romanos", "1 Corintios", "2 Corintios", "Gálatas", "Efesios",
            "Filipenses", "Colosenses", "1 Tesalonicenses", "2 Tesalonicenses", "1 Timoteo",
            "2 Timoteo", "Tito", "Filemón", "Hebreos", "Santiago",
            "1 Pedro", "2 Pedro", "1 Juan", "2 Juan", "3 Juan",
            "Judas", "Apocalipsis"
    };

    public static final String[] SPANISH_ABBREVS = {
            "Gén", "Éxo", "Lev", "Núm", "Deu",
            "Jos", "Jue", "Rut", "1Sa", "2Sa",
            "1Re", "2Re", "1Cr", "2Cr", "Esd",
            "Neh", "Est", "Job", "Sal", "Pro",
            "Ecl", "Cnt", "Isa", "Jer", "Lam",
            "Eze", "Dan", "Ose", "Joe", "Amó",
            "Abd", "Jon", "Miq", "Nah", "Hab",
            "Sof", "Hag", "Zac", "Mal",
            // Nuevo Testamento
            "Mat", "Mar", "Luc", "Jn", "Hch",
            "Rom", "1Co", "2Co", "Gál", "Efe",
            "Fil", "Col", "1Te", "2Te", "1Ti",
            "2Ti", "Tit", "Flm", "Heb", "Stg",
            "1Pe", "2Pe", "1Jn", "2Jn", "3Jn",
            "Jud", "Apo"
    };

    private static final Pattern LEADING_CAPS = Pattern.compile(
            "^([A-ZÁÉÍÓÚÑ]{2,}(?:\\s+[A-ZÁÉÍÓÚÑ]{2,})?|^Y\\s+[A-ZÁÉÍÓÚÑ]{2,})"
    );

    public static void load() {
        if (!BOOKS.isEmpty()) return;

        try (var stream = BibleData.class.getResourceAsStream("/assets/spirit/bible/bible.json")) {
            if (stream == null) {
                Spirit.LOGGER.error("No se encontró el archivo bible.json");
                return;
            }

            var reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            var booksArray = com.google.gson.JsonParser.parseReader(reader).getAsJsonArray();

            int bookIndex = 0;
            for (var bookElem : booksArray) {
                var bookObj = bookElem.getAsJsonObject();
                String name = (bookIndex < SPANISH_NAMES.length) ? SPANISH_NAMES[bookIndex] : bookObj.get("name").getAsString();
                String abbrev = (bookIndex < SPANISH_ABBREVS.length) ? SPANISH_ABBREVS[bookIndex] : bookObj.get("abbrev").getAsString();

                List<List<String>> chapters = new ArrayList<>();

                for (var chElem : bookObj.getAsJsonArray("chapters")) {
                    List<String> verses = new ArrayList<>();
                    for (var vElem : chElem.getAsJsonArray()) {
                        verses.add(cleanVerse(vElem.getAsString()));
                    }
                    chapters.add(verses);
                }

                BOOKS.add(new Book(name, abbrev, chapters));
                bookIndex++;
            }

            Spirit.LOGGER.info("¡Biblia cargada con éxito! Total de libros: {}", BOOKS.size());
        } catch (Exception e) {
            Spirit.LOGGER.error("Error al cargar la Biblia", e);
        }
    }

    private static String cleanVerse(String text) {
        if (text == null || text.isEmpty()) return text;

        text = text.replaceAll("(?<=\\s)[áÁ](?=\\s|[.,;:!?)])", "a")
                .replaceAll("^[áÁ](?=\\s)", "A")
                .replaceAll("(?<=\\s)[óÓ](?=\\s|[.,;:!?)])", "o")
                .replaceAll("\\bfué\\b", "fue")
                .replaceAll("\\bFué\\b", "Fue")
                .replaceAll("\\bdió\\b", "dio")
                .replaceAll("\\bDió\\b", "Dio")
                .replaceAll("\\bvió\\b", "vio")
                .replaceAll("\\bVió\\b", "Vio");

        var matcher = LEADING_CAPS.matcher(text);
        if (matcher.find()) {
            String prefix = matcher.group(1);
            String rest = text.substring(prefix.length());
            String[] words = prefix.split("\\s+");
            StringBuilder fixed = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                String w = words[i];
                if (w.equalsIgnoreCase("JEHOVA")) {
                    fixed.append("Jehová");
                } else if (w.equalsIgnoreCase("DIOS")) {
                    fixed.append("Dios");
                } else if (i == 0) {
                    fixed.append(Character.toUpperCase(w.charAt(0)))
                            .append(w.substring(1).toLowerCase());
                } else {
                    fixed.append(w.toLowerCase());
                }
                if (i < words.length - 1) fixed.append(" ");
            }
            text = fixed.toString() + rest;
        }

        return text;
    }
}
