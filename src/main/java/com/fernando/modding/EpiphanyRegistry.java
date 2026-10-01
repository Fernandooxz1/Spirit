package com.fernando.modding;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.List;
import java.util.Optional;

/**
 * Registro Canónico de Epifanías Bíblicas para Spirit (NeoForge 1.21.1).
 * Contiene 26 pasajes icónicos de la Biblia Reina-Valera asociados a dones espirituales.
 */
public class EpiphanyRegistry {

    public record Epiphany(
            String id,
            String section,
            int bookIndex,
            String bookName,
            int chapterIndex,
            int verseIndex,
            int chapterNumber,
            int verseNumber,
            Holder<MobEffect> effect,
            int durationTicks,
            int amplifier,
            String title,
            String citation,
            String text
    ) {
        public String formatTooltip() {
            return "§6" + title + " §7(§e" + citation + "§7)";
        }
    }

    public static final List<Epiphany> EPIPHANIES = List.of(
            // 1. Pentateuco
            new Epiphany("genesis_shield", "Pentateuco", 0, "Génesis", 14, 0, 15, 1, MobEffects.DAMAGE_RESISTANCE, 6000, 0, "Escudo y Gran Galardón", "Génesis 15:1", "DESPUÉS de estas cosas fué la palabra de Jehová á Abram en visión, diciendo: No temas, Abram; yo soy tu escudo, y tu galardón sobremanera grande."),
            new Epiphany("exodus_healer", "Pentateuco", 1, "Éxodo", 14, 25, 15, 26, MobEffects.REGENERATION, 6000, 1, "Jehová tu Sanador", "Éxodo 15:26", "Y dijo: Si oyeres atentamente la voz de Jehová tu Dios, é hicieres lo recto delante de sus ojos, y dieres oído á sus mandamientos, y guardares todos sus estatutos, ninguna enfermedad de las que envié á los Egipcios te enviaré á ti; porque yo soy Jehová tu Sanador."),
            new Epiphany("deut_barns", "Pentateuco", 4, "Deuteronomio", 27, 7, 28, 8, MobEffects.LUCK, 6000, 1, "Bendición en los Graneros", "Deuteronomio 28:8", "Enviará Jehová contigo la bendición en tus graneros, y en todo aquello en que pusieres tu mano; y te bendecirá en la tierra que Jehová tu Dios te da."),
            new Epiphany("deut_arms", "Pentateuco", 4, "Deuteronomio", 32, 26, 33, 27, MobEffects.ABSORPTION, 6000, 1, "Brazos Eternos", "Deuteronomio 33:27", "El eterno Dios es tu refugio Y acá abajo los brazos eternos; El echará de delante de ti al enemigo, Y dirá: Destruye."),

            // 2. Libros Históricos
            new Epiphany("joshua_courage", "Libros Históricos", 5, "Josué", 0, 8, 1, 9, MobEffects.DAMAGE_BOOST, 6000, 0, "Esfuérzate y sé Valiente", "Josué 1:9", "Mira que te mando que te esfuerces y seas valiente: no temas ni desmayes, porque Jehová tu Dios será contigo en donde quiera que fueres."),
            new Epiphany("samuel_hinds_feet", "Libros Históricos", 9, "2 Samuel", 21, 33, 22, 34, MobEffects.SLOW_FALLING, 6000, 0, "Pies como de Ciervas", "2 Samuel 22:34", "El que hace mis pies como de ciervas, Y el que me asienta en mis alturas;"),
            new Epiphany("kings_strength", "Libros Históricos", 10, "1 Reyes", 18, 7, 19, 8, MobEffects.SATURATION, 6000, 1, "Pan del Camino", "1 Reyes 19:8", "Levantóse pues, y comió y bebió; y caminó con la fortaleza de aquella comida cuarenta días y cuarenta noches, hasta el monte de Dios, Horeb."),

            // 3. Libros Poéticos y Sabiduría
            new Epiphany("psalm_rock", "Libros Poéticos y Sabiduría", 18, "Salmos", 17, 1, 18, 2, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Roca y Castillo Mío", "Salmos 18:2", "Jehová, roca mía y castillo mío, y mi libertador; Dios mío, fuerte mío, en él confiaré; Escudo mío, y el cuerno de mi salud, mi refugio."),
            new Epiphany("psalm_angels", "Libros Poéticos y Sabiduría", 18, "Salmos", 90, 10, 91, 11, MobEffects.SLOW_FALLING, 6000, 0, "Custodia de Ángeles", "Salmos 91:11", "Pues que á sus ángeles mandará acerca de ti, Que te guarden en todos tus caminos."),
            new Epiphany("psalm_healing", "Libros Poéticos y Sabiduría", 18, "Salmos", 102, 2, 103, 3, MobEffects.REGENERATION, 6000, 1, "Sanador de Dolencias", "Salmos 103:3", "El es quien perdona todas tus iniquidades, El que sana todas tus dolencias;"),
            new Epiphany("psalm_lamp", "Libros Poéticos y Sabiduría", 18, "Salmos", 118, 104, 119, 105, MobEffects.NIGHT_VISION, 6000, 0, "Lámpara y Lumbrera", "Salmos 119:105", "NUN. Lámpara es á mis pies tu palabra, Y lumbrera á mi camino."),
            new Epiphany("prov_commit_works", "Libros Poéticos y Sabiduría", 19, "Proverbios", 15, 2, 16, 3, MobEffects.DIG_SPEED, 6000, 1, "Encomienda tus Obras", "Proverbios 16:3", "Encomienda á Jehová tus obras, Y tus pensamientos serán afirmados."),

            // 4. Profetas Mayores y Menores
            new Epiphany("isaiah_eagle_wings", "Profetas Mayores y Menores", 22, "Isaías", 39, 30, 40, 31, MobEffects.MOVEMENT_SPEED, 6000, 1, "Alas de Águila", "Isaías 40:31", "Mas los que esperan á Jehová tendrán nuevas fuerzas; levantarán las alas como águilas, correrán, y no se cansarán, caminarán, y no se fatigarán."),
            new Epiphany("isaiah_waters", "Profetas Mayores y Menores", 22, "Isaías", 42, 1, 43, 2, MobEffects.WATER_BREATHING, 6000, 0, "Paso por las Aguas", "Isaías 43:2", "Cuando pasares por las aguas, yo seré contigo; y por los ríos, no te anegarán. Cuando pasares por el fuego, no te quemarás, ni la llama arderá en ti."),
            new Epiphany("isaiah_wounds", "Profetas Mayores y Menores", 22, "Isaías", 52, 4, 53, 5, MobEffects.REGENERATION, 6000, 1, "Por su Llaga Curados", "Isaías 53:5", "Mas él herido fué por nuestras rebeliones, molido por nuestros pecados: el castigo de nuestra paz sobre él; y por su llaga fuimos nosotros curados."),
            new Epiphany("daniel_furnace", "Profetas Mayores y Menores", 26, "Daniel", 2, 24, 3, 25, MobEffects.FIRE_RESISTANCE, 6000, 0, "Cuatro en el Horno de Fuego", "Daniel 3:25", "Respondió él y dijo: He aquí que yo veo cuatro varones sueltos, que se pasean en medio del fuego, y ningún daño hay en ellos: y el parecer del cuarto es semejante á hijo de los dioses."),
            new Epiphany("malachi_windows", "Profetas Mayores y Menores", 38, "Malaquías", 2, 9, 3, 10, MobEffects.LUCK, 6000, 1, "Ventanas de los Cielos", "Malaquías 3:10", "Traed todos los diezmos al alfolí, y haya alimento en mi casa; y probadme ahora en esto, dice Jehová de los ejércitos, si no os abriré las ventanas de los cielos, y vaciaré sobre vosotros bendición hasta que sobreabunde."),

            // 5. Evangelios y Hechos
            new Epiphany("luke_authority", "Evangelios y Hechos", 41, "Lucas", 9, 18, 10, 19, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Potestad Inmune", "Lucas 10:19", "He aquí os doy potestad de hollar sobre las serpientes y sobre los escorpiones, y sobre toda fuerza del enemigo, y nada os dañará."),
            new Epiphany("john_bread_of_life", "Evangelios y Hechos", 42, "Juan", 5, 34, 6, 35, MobEffects.SATURATION, 6000, 1, "Pan de Vida", "Juan 6:35", "Y Jesús les dijo: Yo soy el pan de vida: el que á mí viene, nunca tendrá hambre; y el que en mí cree, no tendrá sed jamás."),
            new Epiphany("john_light_of_world", "Evangelios y Hechos", 42, "Juan", 7, 11, 8, 12, MobEffects.NIGHT_VISION, 6000, 0, "Luz del Mundo", "Juan 8:12", "Y hablóles Jesús otra vez, diciendo: Yo soy la luz del mundo: el que me sigue, no andará en tinieblas, mas tendrá la lumbre de la vida."),
            new Epiphany("acts_holy_spirit", "Evangelios y Hechos", 43, "Hechos", 0, 7, 1, 8, MobEffects.DAMAGE_BOOST, 6000, 1, "Poder del Espíritu", "Hechos 1:8", "Mas recibiréis la virtud del Espíritu Santo que vendrá sobre vosotros; y me sereís testigos en Jerusalem, en toda Judea, y Samaria, y hasta lo último de la tierra."),

            // 6. Epístolas Paulinas y Generales
            new Epiphany("romans_god_for_us", "Epístolas Paulinas y Generales", 44, "Romanos", 7, 30, 8, 31, MobEffects.ABSORPTION, 6000, 1, "Dios por Nosotros", "Romanos 8:31", "¿Pues qué diremos á esto? Si Dios por nosotros, ¿quién contra nosotros?"),
            new Epiphany("cor_steadfast_labor", "Epístolas Paulinas y Generales", 45, "1 Corintios", 14, 57, 15, 58, MobEffects.DIG_SPEED, 6000, 1, "Firmeza en la Obra", "1 Corintios 15:58", "Así que, hermanos míos amados, estad firmes y constantes, creciendo en la obra del Señor siempre, sabiendo que vuestro trabajo en el Señor no es vano."),
            new Epiphany("ephesians_shield_faith", "Epístolas Paulinas y Generales", 48, "Efesios", 5, 15, 6, 16, MobEffects.FIRE_RESISTANCE, 6000, 0, "Escudo de la Fe", "Efesios 6:16", "Sobre todo, tomando el escudo de la fe, con que podáis apagar todos los dardos de fuego del maligno."),
            new Epiphany("phil_christ_strengthens", "Epístolas Paulinas y Generales", 49, "Filipenses", 3, 12, 4, 13, MobEffects.DAMAGE_BOOST, 6000, 1, "Cristo me Fortalece", "Filipenses 4:13", "Todo lo puedo en Cristo que me fortalece."),

            // 7. Apocalipsis
            new Epiphany("rev_river_of_life", "Apocalipsis", 65, "Apocalipsis", 21, 0, 22, 1, MobEffects.WATER_BREATHING, 6000, 0, "Río de Agua de Vida", "Apocalipsis 22:1", "DESPUÉS me mostró un río limpio de agua de vida, resplandeciente como cristal, que salía del trono de Dios y del Cordero.")
    );

    public static Optional<Epiphany> findByLocation(int bookIndex, int chapterIndex, int verseIndex) {
        return EPIPHANIES.stream()
                .filter(e -> e.bookIndex() == bookIndex && e.chapterIndex() == chapterIndex && e.verseIndex() == verseIndex)
                .findFirst();
    }

    public static Optional<Epiphany> findByChapter(int bookIndex, int chapterIndex) {
        return EPIPHANIES.stream()
                .filter(e -> e.bookIndex() == bookIndex && e.chapterIndex() == chapterIndex)
                .findFirst();
    }

    public static Optional<Epiphany> findById(String id) {
        return EPIPHANIES.stream()
                .filter(e -> e.id().equals(id))
                .findFirst();
    }
}
