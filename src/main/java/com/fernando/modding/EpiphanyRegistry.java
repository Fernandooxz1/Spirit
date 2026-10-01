package com.fernando.modding;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.List;
import java.util.Optional;

/**
 * Registro Canónico de Epifanías Bíblicas para Spirit (NeoForge 1.21.1).
 * Contiene 112 pasajes icónicos de la Biblia Reina-Valera asociados a dones espirituales.
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
            // 1. Pentateuco (14 revelaciones)
            new Epiphany("genesis_light", "Pentateuco", 0, "Génesis", 0, 2, 1, 3, MobEffects.NIGHT_VISION, 6000, 0, "Hágase la Luz", "Génesis 1:3", "Y dijo Dios: Sea la luz: y fué la luz."),
            new Epiphany("genesis_shield", "Pentateuco", 0, "Génesis", 14, 0, 15, 1, MobEffects.DAMAGE_RESISTANCE, 6000, 0, "Escudo y Gran Galardón", "Génesis 15:1", "DESPUÉS de estas cosas fué la palabra de Jehová á Abram en visión, diciendo: No temas, Abram; yo soy tu escudo, y tu galardón sobremanera grande."),
            new Epiphany("genesis_abraham_blessing", "Pentateuco", 0, "Génesis", 21, 16, 22, 17, MobEffects.LUCK, 6000, 1, "Bendición de Abraham", "Génesis 22:17", "Bendiciendo te bendeciré, y multiplicando multiplicaré tu simiente como las estrellas del cielo, y como la arena que está á la orilla del mar; y tu simiente poseerá las puertas de sus enemigos:"),
            new Epiphany("genesis_bethel_presence", "Pentateuco", 0, "Génesis", 27, 14, 28, 15, MobEffects.ABSORPTION, 6000, 1, "Presencia y Guarda", "Génesis 28:15", "Y he aquí, yo soy contigo, y te guardaré por donde quiera que fueres, y te volveré á esta tierra; porque no te dejaré hasta tanto que haya hecho lo que te he dicho."),
            new Epiphany("exodus_pillar_cloud_fire", "Pentateuco", 1, "Éxodo", 12, 20, 13, 21, MobEffects.FIRE_RESISTANCE, 6000, 0, "Columna de Fuego y Nube", "Éxodo 13:21", "Y Jehová iba delante de ellos de día en una columna de nube, para guiarlos por el camino; y de noche en una columna de fuego para alumbrarles; á fin de que anduviesen de día y de noche."),
            new Epiphany("exodus_healer", "Pentateuco", 1, "Éxodo", 14, 25, 15, 26, MobEffects.REGENERATION, 6000, 1, "Jehová tu Sanador", "Éxodo 15:26", "Y dijo: Si oyeres atentamente la voz de Jehová tu Dios, é hicieres lo recto delante de sus ojos, y dieres oído á sus mandamientos, y guardares todos sus estatutos, ninguna enfermedad de las que envié á los Egipcios te enviaré á ti; porque yo soy Jehová tu Sanador."),
            new Epiphany("exodus_manna", "Pentateuco", 1, "Éxodo", 15, 3, 16, 4, MobEffects.SATURATION, 6000, 1, "Pan del Cielo", "Éxodo 16:4", "Y Jehová dijo á Moisés: He aquí yo os haré llover pan del cielo; y el pueblo saldrá, y cogerá para cada un día, para que yo le pruebe si anda en mi ley, ó no."),
            new Epiphany("exodus_rock_water", "Pentateuco", 1, "Éxodo", 16, 5, 17, 6, MobEffects.WATER_BREATHING, 6000, 0, "Agua de la Peña de Horeb", "Éxodo 17:6", "He aquí que yo estoy delante de ti allí sobre la peña en Horeb; y herirás la peña, y saldrán de ella aguas, y beberá el pueblo. Y Moisés lo hizo así en presencia de los ancianos de Israel."),
            new Epiphany("exodus_eagle_wings", "Pentateuco", 1, "Éxodo", 18, 3, 19, 4, MobEffects.MOVEMENT_SPEED, 6000, 1, "Sobre Alas de Águilas", "Éxodo 19:4", "Vosotros visteis lo que hice á los Egipcios, y cómo os tomé sobre alas de águilas, y os he traído á mí."),
            new Epiphany("exodus_bezalel_craft", "Pentateuco", 1, "Éxodo", 30, 2, 31, 3, MobEffects.DIG_SPEED, 6000, 1, "Espíritu de Artificio y Sabiduría", "Éxodo 31:3", "Y lo he henchido de espíritu de Dios, en sabiduría, y en inteligencia, y en ciencia, y en todo artificio,"),
            new Epiphany("numbers_priestly_blessing", "Pentateuco", 3, "Números", 5, 23, 6, 24, MobEffects.DAMAGE_RESISTANCE, 6000, 0, "Bendición Sacerdotal", "Números 6:24", "Jehová te bendiga, y te guarde:"),
            new Epiphany("deut_barns", "Pentateuco", 4, "Deuteronomio", 27, 7, 28, 8, MobEffects.LUCK, 6000, 1, "Bendición en los Graneros", "Deuteronomio 28:8", "Enviará Jehová contigo la bendición en tus graneros, y en todo aquello en que pusieres tu mano; y te bendecirá en la tierra que Jehová tu Dios te da."),
            new Epiphany("deut_be_strong", "Pentateuco", 4, "Deuteronomio", 30, 5, 31, 6, MobEffects.DAMAGE_BOOST, 6000, 0, "Cobrad Ánimo", "Deuteronomio 31:6", "Esforzaos y cobrad ánimo; no temáis, ni tengáis miedo de ellos: que Jehová tu Dios es el que va contigo: no te dejará ni te desamparará."),
            new Epiphany("deut_arms", "Pentateuco", 4, "Deuteronomio", 32, 26, 33, 27, MobEffects.ABSORPTION, 6000, 1, "Brazos Eternos", "Deuteronomio 33:27", "El eterno Dios es tu refugio Y acá abajo los brazos eternos; El echará de delante de ti al enemigo, Y dirá: Destruye."),

            // 2. Libros Históricos (18 revelaciones)
            new Epiphany("joshua_courage", "Libros Históricos", 5, "Josué", 0, 8, 1, 9, MobEffects.DAMAGE_BOOST, 6000, 0, "Esfuérzate y sé Valiente", "Josué 1:9", "Mira que te mando que te esfuerces y seas valiente: no temas ni desmayes, porque Jehová tu Dios será contigo en donde quiera que fueres."),
            new Epiphany("joshua_jordan_crossing", "Libros Históricos", 5, "Josué", 2, 16, 3, 17, MobEffects.WATER_BREATHING, 6000, 0, "Paso del Jordán en Seco", "Josué 3:17", "Mas los sacerdotes que llevaban el arca del pacto de Jehová, estuvieron en seco, firmes en medio del Jordán, hasta que todo el pueblo hubo acabado de pasar el Jordán; y todo Israel pasó en seco."),
            new Epiphany("joshua_jericho_walls", "Libros Históricos", 5, "Josué", 5, 19, 6, 20, MobEffects.DAMAGE_BOOST, 6000, 1, "Caída de los Muros de Jericó", "Josué 6:20", "Entonces el pueblo dió grita, y los sacerdotes tocaron las bocinas: y aconteció que como el pueblo hubo oído el sonido de la bocina, dió el pueblo grita con gran vocerío, y el muro cayó á plomo. El pueblo subió luego á la ciudad, cada uno en derecho de sí, y tomáronla."),
            new Epiphany("joshua_sun_stand_still", "Libros Históricos", 5, "Josué", 9, 11, 10, 12, MobEffects.NIGHT_VISION, 6000, 0, "Sol en Gabaón", "Josué 10:12", "Entonces Josué habló á Jehová el día que Jehová entregó al Amorrheo delante de los hijos de Israel, y dijo en presencia de los Israelitas: Sol, detente en Gabaón; Y tú, Luna, en el valle de Ajalón."),
            new Epiphany("judges_gideon_three_hundred", "Libros Históricos", 6, "Jueces", 6, 6, 7, 7, MobEffects.ABSORPTION, 6000, 1, "Los Trescientos de Gedeón", "Jueces 7:7", "Entonces Jehová dijo á Gedeón: Con estos trescientos hombres que lamieron el agua os salvaré, y entregaré á los Madianitas en tus manos: y váyase toda la gente cada uno á su lugar."),
            new Epiphany("judges_samson_bonds", "Libros Históricos", 6, "Jueces", 14, 13, 15, 14, MobEffects.DAMAGE_BOOST, 6000, 1, "Fuerza Quebrantadora de Sansón", "Jueces 15:14", "Y así que vino hasta Lehi, los Filisteos le salieron á recibir con algazara: y el espíritu de Jehová cayó sobre él, y las cuerdas que estaban en sus brazos se tornaron como lino quemado con fuego, y las ataduras se cayeron de sus manos."),
            new Epiphany("ruth_full_reward", "Libros Históricos", 7, "Rut", 1, 11, 2, 12, MobEffects.LUCK, 6000, 1, "Plena Recompensa", "Rut 2:12", "Jehová galardone tu obra, y tu remuneración sea llena por Jehová Dios de Israel, que has venido para cubrirte debajo de sus alas."),
            new Epiphany("samuel_david_goliath", "Libros Históricos", 8, "1 Samuel", 16, 44, 17, 45, MobEffects.DAMAGE_BOOST, 6000, 1, "En el Nombre de Jehová", "1 Samuel 17:45", "Entonces dijo David al Filisteo: Tú vienes á mí con espada y lanza y escudo; mas yo vengo á ti en el nombre de Jehová de los ejércitos, el Dios de los escuadrones de Israel, que tú has provocado."),
            new Epiphany("samuel_hinds_feet", "Libros Históricos", 9, "2 Samuel", 21, 33, 22, 34, MobEffects.SLOW_FALLING, 6000, 0, "Pies como de Ciervas", "2 Samuel 22:34", "El que hace mis pies como de ciervas, Y el que me asienta en mis alturas;"),
            new Epiphany("kings_elijah_ravens", "Libros Históricos", 10, "1 Reyes", 16, 5, 17, 6, MobEffects.SATURATION, 6000, 1, "Sustento en el Querith", "1 Reyes 17:6", "Y los cuervos le traían pan y carne por la mañana, y pan y carne á la tarde; y bebía del arroyo."),
            new Epiphany("kings_elijah_supernatural_run", "Libros Históricos", 10, "1 Reyes", 17, 45, 18, 46, MobEffects.MOVEMENT_SPEED, 6000, 1, "Carrera de Elías", "1 Reyes 18:46", "Y la mano de Jehová fué sobre Elías, el cual ciñó sus lomos, y vino corriendo delante de Achâb hasta llegar á Jezreel."),
            new Epiphany("kings_strength", "Libros Históricos", 10, "1 Reyes", 18, 7, 19, 8, MobEffects.SATURATION, 6000, 1, "Pan del Camino", "1 Reyes 19:8", "Levantóse pues, y comió y bebió; y caminó con la fortaleza de aquella comida cuarenta días y cuarenta noches, hasta el monte de Dios, Horeb."),
            new Epiphany("kings_chariot_of_fire", "Libros Históricos", 11, "2 Reyes", 1, 10, 2, 11, MobEffects.FIRE_RESISTANCE, 6000, 0, "Carro de Fuego de Elías", "2 Reyes 2:11", "Y aconteció que, yendo ellos hablando, he aquí, un carro de fuego con caballos de fuego apartó á los dos: y Elías subió al cielo en un torbellino."),
            new Epiphany("kings_elisha_mountain_fire", "Libros Históricos", 11, "2 Reyes", 5, 16, 6, 17, MobEffects.NIGHT_VISION, 6000, 0, "Carros de Fuego en Dotán", "2 Reyes 6:17", "Y oró Eliseo, y dijo: Ruégote, oh Jehová, que abras sus ojos para que vea. Entonces Jehová abrió los ojos del mozo, y miró: y he aquí que el monte estaba lleno de gente de á caballo, y de carros de fuego alrededor de Eliseo."),
            new Epiphany("chron_jabez_blessing", "Libros Históricos", 12, "1 Crónicas", 3, 9, 4, 10, MobEffects.LUCK, 6000, 1, "Oración de Jabes", "1 Crónicas 4:10", "E invocó Jabes al Dios de Israel, diciendo: ¡Oh si me dieras bendición, y ensancharas mi término, y si tu mano fuera conmigo, y me libraras de mal, que no me dañe! E hizo Dios que le viniese lo que pidió."),
            new Epiphany("chron_battle_is_gods", "Libros Históricos", 13, "2 Crónicas", 19, 14, 20, 15, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "La Batalla es de Dios", "2 Crónicas 20:15", "Y dijo: Oid, Judá todo, y vosotros moradores de Jerusalem, y tú, rey Josaphat. Jehová os dice así: No temáis ni os amedrentéis delante de esta tan grande multitud; porque no es vuestra la guerra, sino de Dios."),
            new Epiphany("nehemiah_joy_strength", "Libros Históricos", 15, "Nehemías", 7, 9, 8, 10, MobEffects.DAMAGE_BOOST, 6000, 0, "El Gozo del Señor", "Nehemías 8:10", "Díjoles luego: Id, comed grosuras, y bebed vino dulce, y enviad porciones á los que no tienen prevenido; porque día santo es á nuestro Señor: y no os entristezcáis, porque el gozo de Jehová es vuestra fortaleza."),
            new Epiphany("esther_for_such_a_time", "Libros Históricos", 16, "Ester", 3, 13, 4, 14, MobEffects.LUCK, 6000, 1, "Para Esta Hora", "Ester 4:14", "Porque si absolutamente callares en este tiempo, respiro y libertación tendrán los Judíos de otra parte; mas tú y la casa de tu padre pereceréis. ¿Y quién sabe si para esta hora te han hecho llegar al reino?"),

            // 3. Libros Poéticos y Sabiduría (14 revelaciones)
            new Epiphany("job_redeemer_lives", "Libros Poéticos y Sabiduría", 17, "Job", 18, 24, 19, 25, MobEffects.REGENERATION, 6000, 1, "Mi Redentor Vive", "Job 19:25", "Yo sé que mi Redentor vive, Y al fin se levantará sobre el polvo:"),
            new Epiphany("psalm_rock", "Libros Poéticos y Sabiduría", 18, "Salmos", 17, 1, 18, 2, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Roca y Castillo Mío", "Salmos 18:2", "Jehová, roca mía y castillo mío, y mi libertador; Dios mío, fuerte mío, en él confiaré; Escudo mío, y el cuerno de mi salud, mi refugio."),
            new Epiphany("psalm_shepherd", "Libros Poéticos y Sabiduría", 18, "Salmos", 22, 0, 23, 1, MobEffects.SATURATION, 6000, 1, "El Buen Pastor", "Salmos 23:1", "JEHOVA es mi pastor; nada me faltará."),
            new Epiphany("psalm_light_salvation", "Libros Poéticos y Sabiduría", 18, "Salmos", 26, 0, 27, 1, MobEffects.NIGHT_VISION, 6000, 0, "Luz y Salvación", "Salmos 27:1", "JEHOVA es mi luz y mi salvación: ¿de quién temeré? Jehová es la fortaleza de mi vida: ¿de quién he de atemorizarme?"),
            new Epiphany("psalm_angel_encamps", "Libros Poéticos y Sabiduría", 18, "Salmos", 33, 6, 34, 7, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Campamento de Ángeles", "Salmos 34:7", "El ángel de Jehová acampa en derredor de los que le temen, Y los defiende."),
            new Epiphany("psalm_refuge_strength", "Libros Poéticos y Sabiduría", 18, "Salmos", 45, 0, 46, 1, MobEffects.ABSORPTION, 6000, 1, "Amparo y Pronto Auxilio", "Salmos 46:1", "DIOS es nuestro amparo y fortaleza, Nuestro pronto auxilio en las tribulaciones."),
            new Epiphany("psalm_angels", "Libros Poéticos y Sabiduría", 18, "Salmos", 90, 10, 91, 11, MobEffects.SLOW_FALLING, 6000, 0, "Custodia de Ángeles", "Salmos 91:11", "Pues que á sus ángeles mandará acerca de ti, Que te guarden en todos tus caminos."),
            new Epiphany("psalm_healing", "Libros Poéticos y Sabiduría", 18, "Salmos", 102, 2, 103, 3, MobEffects.REGENERATION, 6000, 1, "Sanador de Dolencias", "Salmos 103:3", "El es quien perdona todas tus iniquidades, El que sana todas tus dolencias;"),
            new Epiphany("psalm_lamp", "Libros Poéticos y Sabiduría", 18, "Salmos", 118, 104, 119, 105, MobEffects.NIGHT_VISION, 6000, 0, "Lámpara y Lumbrera", "Salmos 119:105", "NUN. Lámpara es á mis pies tu palabra, Y lumbrera á mi camino."),
            new Epiphany("psalm_keeper_of_israel", "Libros Poéticos y Sabiduría", 18, "Salmos", 120, 6, 121, 7, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "El Guardador Fiel", "Salmos 121:7", "Jehová te guardará de todo mal: El guardará tu alma."),
            new Epiphany("prov_straight_paths", "Libros Poéticos y Sabiduría", 19, "Proverbios", 2, 5, 3, 6, MobEffects.MOVEMENT_SPEED, 6000, 1, "Veredas Guiadas", "Proverbios 3:6", "Reconócelo en todos tus caminos, Y él enderezará tus veredas."),
            new Epiphany("prov_commit_works", "Libros Poéticos y Sabiduría", 19, "Proverbios", 15, 2, 16, 3, MobEffects.DIG_SPEED, 6000, 1, "Encomienda tus Obras", "Proverbios 16:3", "Encomienda á Jehová tus obras, Y tus pensamientos serán afirmados."),
            new Epiphany("prov_strong_tower", "Libros Poéticos y Sabiduría", 19, "Proverbios", 17, 9, 18, 10, MobEffects.ABSORPTION, 6000, 1, "Torre Fuerte", "Proverbios 18:10", "Torre fuerte es el nombre de Jehová: A él correrá el justo, y será levantado."),
            new Epiphany("eccl_threefold_cord", "Libros Poéticos y Sabiduría", 20, "Eclesiastés", 3, 11, 4, 12, MobEffects.DAMAGE_RESISTANCE, 6000, 0, "Cordón de Tres Dobleces", "Eclesiastés 4:12", "Y si alguno prevaleciere contra el uno, dos estarán contra él; y cordón de tres dobleces no presto se rompe."),

            // 4. Profetas Mayores y Menores (18 revelaciones)
            new Epiphany("isaiah_white_as_snow", "Profetas Mayores y Menores", 22, "Isaías", 0, 17, 1, 18, MobEffects.REGENERATION, 6000, 1, "Blanco como la Nieve", "Isaías 1:18", "Venid luego, dirá Jehová, y estemos á cuenta: si vuestros pecados fueren como la grana, como la nieve serán emblanquecidos: si fueren rojos como el carmesí, vendrán á ser como blanca lana."),
            new Epiphany("isaiah_eagle_wings", "Profetas Mayores y Menores", 22, "Isaías", 39, 30, 40, 31, MobEffects.MOVEMENT_SPEED, 6000, 1, "Alas de Águila", "Isaías 40:31", "Mas los que esperan á Jehová tendrán nuevas fuerzas; levantarán las alas como águilas, correrán, y no se cansarán, caminarán, y no se fatigarán."),
            new Epiphany("isaiah_fear_not_upheld", "Profetas Mayores y Menores", 22, "Isaías", 40, 9, 41, 10, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Sustento de la Diestra", "Isaías 41:10", "No temas, que yo soy contigo; no desmayes, que yo soy tu Dios que te esfuerzo: siempre te ayudaré, siempre te sustentaré con la diestra de mi justicia."),
            new Epiphany("isaiah_waters", "Profetas Mayores y Menores", 22, "Isaías", 42, 1, 43, 2, MobEffects.WATER_BREATHING, 6000, 0, "Paso por las Aguas", "Isaías 43:2", "Cuando pasares por las aguas, yo seré contigo; y por los ríos, no te anegarán. Cuando pasares por el fuego, no te quemarás, ni la llama arderá en ti."),
            new Epiphany("isaiah_water_on_thirsty", "Profetas Mayores y Menores", 22, "Isaías", 43, 2, 44, 3, MobEffects.WATER_BREATHING, 6000, 0, "Aguas sobre el Secadal", "Isaías 44:3", "Porque yo derramaré aguas sobre el secadal, y ríos sobre la tierra árida: mi espíritu derramaré sobre tu generación, y mi bendición sobre tus renuevos:"),
            new Epiphany("isaiah_wounds", "Profetas Mayores y Menores", 22, "Isaías", 52, 4, 53, 5, MobEffects.REGENERATION, 6000, 1, "Por su Llaga Curados", "Isaías 53:5", "Mas él herido fué por nuestras rebeliones, molido por nuestros pecados: el castigo de nuestra paz sobre él; y por su llaga fuimos nosotros curados."),
            new Epiphany("isaiah_no_weapon_prosper", "Profetas Mayores y Menores", 22, "Isaías", 53, 16, 54, 17, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Inmunidad del Siervo", "Isaías 54:17", "Toda herramienta que fuere fabricada contra ti, no prosperará; y tú condenarás toda lengua que se levantare contra ti en juicio. Esta es la heredad de los siervos de Jehová, y su justicia de por mí, dijo Jehová."),
            new Epiphany("jeremiah_thoughts_of_peace", "Profetas Mayores y Menores", 23, "Jeremías", 28, 10, 29, 11, MobEffects.LUCK, 6000, 1, "Pensamientos de Paz", "Jeremías 29:11", "Porque yo sé los pensamientos que tengo acerca de vosotros, dice Jehová, pensamientos de paz, y no de mal, para daros el fin que esperáis."),
            new Epiphany("jeremiah_call_unto_me", "Profetas Mayores y Menores", 23, "Jeremías", 32, 2, 33, 3, MobEffects.NIGHT_VISION, 6000, 0, "Revelación Oculta", "Jeremías 33:3", "Clama á mí, y te responderé, y te enseñaré cosas grandes y dificultosas que tú no sabes."),
            new Epiphany("lam_mercies_new_morning", "Profetas Mayores y Menores", 24, "Lamentaciones", 2, 22, 3, 23, MobEffects.REGENERATION, 6000, 1, "Nuevas Cada Mañana", "Lamentaciones 3:23", "Nuevas son cada mañana; grande es tu fidelidad."),
            new Epiphany("ezekiel_heart_of_flesh", "Profetas Mayores y Menores", 25, "Ezequiel", 35, 25, 36, 26, MobEffects.REGENERATION, 6000, 1, "Corazón de Carne", "Ezequiel 36:26", "Y os daré corazón nuevo, y pondré espíritu nuevo dentro de vosotros; y quitaré de vuestra carne el corazón de piedra, y os daré corazón de carne."),
            new Epiphany("daniel_furnace", "Profetas Mayores y Menores", 26, "Daniel", 2, 24, 3, 25, MobEffects.FIRE_RESISTANCE, 6000, 0, "Cuatro en el Horno de Fuego", "Daniel 3:25", "Respondió él y dijo: He aquí que yo veo cuatro varones sueltos, que se pasean en medio del fuego, y ningún daño hay en ellos: y el parecer del cuarto es semejante á hijo de los dioses."),
            new Epiphany("daniel_lions_den", "Profetas Mayores y Menores", 26, "Daniel", 5, 21, 6, 22, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Ángel en el Foso de Leones", "Daniel 6:22", "El Dios mío envió su ángel, el cual cerró la boca de los leones, para que no me hiciesen mal: porque delante de él se halló en mí justicia: y aun delante de ti, oh rey, yo no he hecho lo que no debiese."),
            new Epiphany("joel_spirit_poured_out", "Profetas Mayores y Menores", 28, "Joel", 1, 27, 2, 28, MobEffects.NIGHT_VISION, 6000, 0, "Visión del Espíritu", "Joel 2:28", "Y será que después de esto, derramaré mi Espíritu sobre toda carne, y profetizarán vuestros hijos y vuestras hijas; vuestros viejos soñarán sueños, y vuestros mancebos verán visiones."),
            new Epiphany("habakkuk_hinds_feet_heights", "Profetas Mayores y Menores", 34, "Habacuc", 2, 18, 3, 19, MobEffects.JUMP, 6000, 1, "Andar sobre las Alturas", "Habacuc 3:19", "Jehová el Señor es mi fortaleza, El cual pondrá mis pies como de ciervas, Y me hará andar sobre mis alturas"),
            new Epiphany("zechariah_by_my_spirit", "Profetas Mayores y Menores", 37, "Zacarías", 3, 5, 4, 6, MobEffects.DIG_SPEED, 6000, 1, "No con Fuerza, mas con mi Espíritu", "Zacarías 4:6", "Entonces respondió y hablóme, diciendo: Esta es palabra de Jehová á Zorobabel, en que se dice: No con ejército, ni con fuerza, sino con mi espíritu, ha dicho Jehová de los ejércitos."),
            new Epiphany("malachi_windows", "Profetas Mayores y Menores", 38, "Malaquías", 2, 9, 3, 10, MobEffects.LUCK, 6000, 1, "Ventanas de los Cielos", "Malaquías 3:10", "Traed todos los diezmos al alfolí, y haya alimento en mi casa; y probadme ahora en esto, dice Jehová de los ejércitos, si no os abriré las ventanas de los cielos, y vaciaré sobre vosotros bendición hasta que sobreabunde."),
            new Epiphany("malachi_sun_of_righteousness", "Profetas Mayores y Menores", 38, "Malaquías", 3, 1, 4, 2, MobEffects.REGENERATION, 6000, 1, "Sol de Justicia", "Malaquías 4:2", "Mas á vosotros los que teméis mi nombre, nacerá el Sol de justicia, y en sus alas traerá salud: y saldréis, y saltaréis como becerros de la manada."),

            // 5. Evangelios y Hechos (18 revelaciones)
            new Epiphany("matthew_bread_alone", "Evangelios y Hechos", 39, "Mateo", 3, 3, 4, 4, MobEffects.SATURATION, 6000, 1, "No Solo de Pan", "Mateo 4:4", "Mas él respondiendo, dijo: Escrito está: No con solo el pan vivirá el hombre, mas con toda palabra que sale de la boca de Dios."),
            new Epiphany("matthew_light_world", "Evangelios y Hechos", 39, "Mateo", 4, 13, 5, 14, MobEffects.NIGHT_VISION, 6000, 0, "Luz del Mundo", "Mateo 5:14", "Vosotros sois la luz del mundo: una ciudad asentada sobre un monte no se puede esconder."),
            new Epiphany("matthew_seek_first", "Evangelios y Hechos", 39, "Mateo", 5, 32, 6, 33, MobEffects.LUCK, 6000, 1, "Buscad Primeramente el Reino", "Mateo 6:33", "Mas buscad primeramente el reino de Dios y su justicia, y todas estas cosas os serán añadidas."),
            new Epiphany("matthew_heavy_laden", "Evangelios y Hechos", 39, "Mateo", 10, 27, 11, 28, MobEffects.REGENERATION, 6000, 1, "Venid a Mí los Trabajados", "Mateo 11:28", "Venid á mí todos los que estáis trabajados y cargados, que yo os haré descansar."),
            new Epiphany("matthew_walk_water", "Evangelios y Hechos", 39, "Mateo", 13, 28, 14, 29, MobEffects.WATER_BREATHING, 6000, 0, "Caminando Sobre las Aguas", "Mateo 14:29", "Y él dijo: Ven. Y descendiendo Pedro del barco, andaba sobre las aguas para ir á Jesús."),
            new Epiphany("mark_all_possible", "Evangelios y Hechos", 40, "Marcos", 8, 22, 9, 23, MobEffects.DAMAGE_BOOST, 6000, 1, "Al que Cree Todo es Posible", "Marcos 9:23", "Y Jesús le dijo: Si puedes creer, al que cree todo es posible."),
            new Epiphany("mark_faith_prayer", "Evangelios y Hechos", 40, "Marcos", 10, 23, 11, 24, MobEffects.LUCK, 6000, 1, "Fe en la Oración", "Marcos 11:24", "Por tanto, os digo que todo lo que orando pidiereis, creed que lo recibiréis, y os vendrá."),
            new Epiphany("luke_nothing_impossible", "Evangelios y Hechos", 41, "Lucas", 0, 36, 1, 37, MobEffects.DAMAGE_BOOST, 6000, 1, "Nada Hay Imposible para Dios", "Lucas 1:37", "Porque ninguna cosa es imposible para Dios."),
            new Epiphany("luke_faith_healed", "Evangelios y Hechos", 41, "Lucas", 7, 47, 8, 48, MobEffects.REGENERATION, 6000, 1, "Tu Fe te ha Salvado", "Lucas 8:48", "Y él dijo: Hija, tu fe te ha salvado: ve en paz."),
            new Epiphany("luke_authority", "Evangelios y Hechos", 41, "Lucas", 9, 18, 10, 19, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Potestad Inmune", "Lucas 10:19", "He aquí os doy potestad de hollar sobre las serpientes y sobre los escorpiones, y sobre toda fuerza del enemigo, y nada os dañará."),
            new Epiphany("john_living_water", "Evangelios y Hechos", 42, "Juan", 3, 13, 4, 14, MobEffects.WATER_BREATHING, 6000, 0, "Fuente de Agua Viva", "Juan 4:14", "Mas el que bebiere del agua que yo le daré, para siempre no tendrá sed: mas el agua que yo le daré, será en él una fuente de agua que salte para vida eterna."),
            new Epiphany("john_bread_of_life", "Evangelios y Hechos", 42, "Juan", 5, 34, 6, 35, MobEffects.SATURATION, 6000, 1, "Pan de Vida", "Juan 6:35", "Y Jesús les dijo: Yo soy el pan de vida: el que á mí viene, nunca tendrá hambre; y el que en mí cree, no tendrá sed jamás."),
            new Epiphany("john_light_of_world", "Evangelios y Hechos", 42, "Juan", 7, 11, 8, 12, MobEffects.NIGHT_VISION, 6000, 0, "Luz del Mundo", "Juan 8:12", "Y habladoles Jesús otra vez, diciendo: Yo soy la luz del mundo: el que me sigue, no andará en tinieblas, mas tendrá la lumbre de la vida."),
            new Epiphany("john_resurrection_life", "Evangelios y Hechos", 42, "Juan", 10, 24, 11, 25, MobEffects.REGENERATION, 6000, 1, "Resurrección y Vida", "Juan 11:25", "Dícele Jesús: Yo soy la resurrección y la vida: el que cree en mí, aunque esté muerto, vivirá."),
            new Epiphany("john_my_peace", "Evangelios y Hechos", 42, "Juan", 13, 26, 14, 27, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Mi Paz os Doy", "Juan 14:27", "La paz os dejo, mi paz os doy: no como el mundo la da, yo os la doy. No se turbe vuestro corazón, ni tenga miedo."),
            new Epiphany("john_true_vine", "Evangelios y Hechos", 42, "Juan", 14, 4, 15, 5, MobEffects.SATURATION, 6000, 1, "La Vid Verdadera", "Juan 15:5", "Yo soy la vid, vosotros los pámpanos: el que está en mí, y yo en él, éste lleva mucho fruto; porque sin mí nada podéis hacer."),
            new Epiphany("acts_holy_spirit", "Evangelios y Hechos", 43, "Hechos", 0, 7, 1, 8, MobEffects.DAMAGE_BOOST, 6000, 1, "Poder del Espíritu", "Hechos 1:8", "Mas recibiréis poder, cuando haya venido sobre vosotros el Espiritu Santo; y me sereís testigos en Jerusalem, en toda Judea, y Samaria, y hasta lo último de la tierra."),
            new Epiphany("acts_tongues_fire", "Evangelios y Hechos", 43, "Hechos", 1, 2, 2, 3, MobEffects.FIRE_RESISTANCE, 6000, 0, "Lenguas de Fuego", "Hechos 2:3", "Y se les aparecieron lenguas repartidas, como de fuego, que se asentó sobre cada uno de ellos."),

            // 6. Epístolas Paulinas y Generales (26 revelaciones)
            new Epiphany("romans_power_gospel", "Epístolas Paulinas y Generales", 44, "Romanos", 0, 15, 1, 16, MobEffects.DAMAGE_BOOST, 6000, 1, "Potencia del Evangelio", "Romanos 1:16", "Porque no me avergüenzo del evangelio: porque es potencia de Dios para salud á todo aquel que cree; al Judío primeramente y también al Griego."),
            new Epiphany("romans_god_for_us", "Epístolas Paulinas y Generales", 44, "Romanos", 7, 30, 8, 31, MobEffects.ABSORPTION, 6000, 1, "Dios por Nosotros", "Romanos 8:31", "¿Pues qué diremos á esto? Si Dios por nosotros, ¿quién contra nosotros?"),
            new Epiphany("romans_more_conquerors", "Epístolas Paulinas y Generales", 44, "Romanos", 7, 36, 8, 37, MobEffects.DAMAGE_BOOST, 6000, 1, "Más que Vencedores", "Romanos 8:37", "Antes, en todas estas cosas hacemos más que vencer por medio de aquel que nos amó."),
            new Epiphany("romans_overcome_evil", "Epístolas Paulinas y Generales", 44, "Romanos", 11, 20, 12, 21, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Vence el Mal con el Bien", "Romanos 12:21", "No seas vencido de lo malo; mas vence con el bien el mal."),
            new Epiphany("cor_run_the_race", "Epístolas Paulinas y Generales", 45, "1 Corintios", 8, 23, 9, 24, MobEffects.MOVEMENT_SPEED, 6000, 1, "Corred para Obtener", "1 Corintios 9:24", "¿No sabéis que los que corren en el estadio, todos á la verdad corren, mas uno lleva el premio? Corred de tal manera que lo obtengáis."),
            new Epiphany("cor_faithful_god", "Epístolas Paulinas y Generales", 45, "1 Corintios", 9, 12, 10, 13, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Fiel es Dios ante la Prueba", "1 Corintios 10:13", "No os ha tomado tentación, sino humana: mas fiel es Dios, que no os dejará ser tentados más de lo que podeís llevar; antes dará también juntamente con la tentación la salida, para que podáis aguantar."),
            new Epiphany("cor_steadfast_labor", "Epístolas Paulinas y Generales", 45, "1 Corintios", 14, 57, 15, 58, MobEffects.DIG_SPEED, 6000, 1, "Firmeza en la Obra", "1 Corintios 15:58", "Así que, hermanos míos amados, estad firmes y constantes, creciendo en la obra del Señor siempre, sabiendo que vuestro trabajo en el Señor no es vano."),
            new Epiphany("cor_inward_renewed", "Epístolas Paulinas y Generales", 46, "2 Corintios", 3, 15, 4, 16, MobEffects.REGENERATION, 6000, 1, "Hombre Interior Renovado", "2 Corintios 4:16", "Por tanto, no desmayamos: antes aunque este nuestro hombre exterior se va desgastando, el interior empero se renueva de día en día."),
            new Epiphany("cor_grace_sufficient", "Epístolas Paulinas y Generales", 46, "2 Corintios", 11, 8, 12, 9, MobEffects.ABSORPTION, 6000, 1, "Bástate mi Gracia", "2 Corintios 12:9", "Y me ha dicho: Bástate mi gracia; porque mi potencia en la flaqueza se perfecciona. Por tanto, de buena gana me gloriaré más bien en mis flaquezas, porque habite en mí la potencia de Cristo."),
            new Epiphany("galatians_spirit_fruit", "Epístolas Paulinas y Generales", 47, "Gálatas", 4, 21, 5, 22, MobEffects.SATURATION, 6000, 1, "Fruto del Espíritu", "Gálatas 5:22", "Mas el fruto del Espíritu es: caridad, gozo, paz, tolerancia, benignidad, bondad, fe,"),
            new Epiphany("ephesians_exceeding_abundantly", "Epístolas Paulinas y Generales", 48, "Efesios", 2, 19, 3, 20, MobEffects.LUCK, 6000, 1, "Superabundante Poder", "Efesios 3:20", "Y á Aquel que es poderoso para hacer todas las cosas mucho más abundantemente de lo que pedimos ó entendemos, por la potencia que obra en nosotros,"),
            new Epiphany("ephesians_whole_armour", "Epístolas Paulinas y Generales", 48, "Efesios", 5, 10, 6, 11, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Toda la Armadura de Dios", "Efesios 6:11", "Vestíos de toda la armadura de Dios, para que podáis estar firmes contra las asechanzas del diablo."),
            new Epiphany("ephesians_shield_faith", "Epístolas Paulinas y Generales", 48, "Efesios", 5, 15, 6, 16, MobEffects.FIRE_RESISTANCE, 6000, 0, "Escudo de la Fe", "Efesios 6:16", "Sobre todo, tomando el escudo de la fe, con que podáis apagar todos los dardos de fuego del maligno."),
            new Epiphany("phil_christ_strengthens", "Epístolas Paulinas y Generales", 49, "Filipenses", 3, 12, 4, 13, MobEffects.DAMAGE_BOOST, 6000, 1, "Cristo me Fortalece", "Filipenses 4:13", "Todo lo puedo en Cristo que me fortalece."),
            new Epiphany("colossians_strengthened_might", "Epístolas Paulinas y Generales", 50, "Colosenses", 0, 10, 1, 11, MobEffects.DAMAGE_BOOST, 6000, 1, "Corroborados de Fortaleza", "Colosenses 1:11", "Corroborados de toda fortaleza, conforme á la potencia de su gloria, para toda tolerancia y largura de ánimo con gozo;"),
            new Epiphany("thess_children_light", "Epístolas Paulinas y Generales", 51, "1 Tesalonicenses", 4, 4, 5, 5, MobEffects.NIGHT_VISION, 6000, 0, "Hijos de Luz", "1 Tesalonicenses 5:5", "Porque todos vosotros sois hijos de luz, é hijos del día; no somos de la noche, ni de las tinieblas."),
            new Epiphany("thess_faithful_lord", "Epístolas Paulinas y Generales", 52, "2 Tesalonicenses", 2, 2, 3, 3, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Fiel es el Señor", "2 Tesalonicenses 3:3", "Mas fiel es el Señor, que os confirmará y guardará del mal."),
            new Epiphany("timothy_fight_good_fight", "Epístolas Paulinas y Generales", 53, "1 Timoteo", 5, 11, 6, 12, MobEffects.DAMAGE_BOOST, 6000, 1, "La Buena Batalla", "1 Timoteo 6:12", "Pelea la buena batalla de la fe, echa mano de la vida eterna, á la cual asimismo eres llamado, habiendo hecho buena profesión delante de muchos testigos."),
            new Epiphany("timothy_spirit_power", "Epístolas Paulinas y Generales", 54, "2 Timoteo", 0, 6, 1, 7, MobEffects.DAMAGE_BOOST, 6000, 1, "Espíritu de Fortaleza", "2 Timoteo 1:7", "Porque no nos ha dado Dios el espíritu de temor, sino el de fortaleza, y de amor, y de templanza."),
            new Epiphany("hebrews_word_sword", "Epístolas Paulinas y Generales", 57, "Hebreos", 3, 11, 4, 12, MobEffects.DAMAGE_BOOST, 6000, 1, "Espada de Dos Filos", "Hebreos 4:12", "Porque la palabra de Dios es viva y eficaz, y más penetrante que toda espada de dos filos: y que alcanza hasta partir el alma, y aun el espíritu, y las coyunturas y tuétanos, y discierne los pensamientos y las intenciones del corazón."),
            new Epiphany("hebrews_faith_substance", "Epístolas Paulinas y Generales", 57, "Hebreos", 10, 0, 11, 1, MobEffects.ABSORPTION, 6000, 1, "Sustancia de las Cosas", "Hebreos 11:1", "ES pues la fe la sustancia de las cosas que se esperan, la demostración de las cosas que no se ven."),
            new Epiphany("hebrews_run_patience", "Epístolas Paulinas y Generales", 57, "Hebreos", 11, 0, 12, 1, MobEffects.MOVEMENT_SPEED, 6000, 1, "La Carrera con Paciencia", "Hebreos 12:1", "POR tanto nosotros también, teniendo en derredor nuestro una tan grande nube de testigos, dejando todo el peso del pecado que nos rodea, corramos con paciencia la carrera que nos es propuesta,"),
            new Epiphany("james_ask_wisdom", "Epístolas Paulinas y Generales", 58, "Santiago", 0, 4, 1, 5, MobEffects.LUCK, 6000, 1, "Sabiduría sin Reproche", "Santiago 1:5", "Y si alguno de vosotros tiene falta de sabiduría, demándela á Dios, el cual da á todos abundantemente, y no zahiere; y le será dada."),
            new Epiphany("peter_cast_cares", "Epístolas Paulinas y Generales", 59, "1 Pedro", 4, 6, 5, 7, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Toda Vuestra Solicitud", "1 Pedro 5:7", "Echando toda vuestra solicitud en él, porque él tiene cuidado de vosotros."),
            new Epiphany("john_faith_overcomes_world", "Epístolas Paulinas y Generales", 61, "1 Juan", 4, 3, 5, 4, MobEffects.DAMAGE_RESISTANCE, 6000, 1, "Victoria que Vence al Mundo", "1 Juan 5:4", "Porque todo aquello que es nacido de Dios vence al mundo: y esta es la victoria que vence al mundo, nuestra fe."),
            new Epiphany("jude_keep_from_falling", "Epístolas Paulinas y Generales", 64, "Judas", 0, 23, 1, 24, MobEffects.SLOW_FALLING, 6000, 0, "Guardados sin Caída", "Judas 1:24", "A aquel, pues, que es poderoso para guardaros sin caída, y presentaros delante de su gloria irreprensibles, con grande alegría,"),

            // 7. Apocalipsis (4 revelaciones)
            new Epiphany("rev_door_supper", "Apocalipsis", 65, "Apocalipsis", 2, 19, 3, 20, MobEffects.SATURATION, 6000, 1, "Cena con el Cordero", "Apocalipsis 3:20", "He aquí, yo estoy á la puerta y llamo: si alguno oyere mi voz y abriere la puerta, entraré á él, y cenaré con él, y él conmigo."),
            new Epiphany("rev_no_hunger_thirst", "Apocalipsis", 65, "Apocalipsis", 6, 15, 7, 16, MobEffects.SATURATION, 6000, 1, "Ni Hambre ni Sed", "Apocalipsis 7:16", "No tendrán más hambre, ni sed, y el sol no caerá más sobre ellos, ni otro ningún calor."),
            new Epiphany("rev_no_more_death", "Apocalipsis", 65, "Apocalipsis", 20, 3, 21, 4, MobEffects.REGENERATION, 6000, 1, "Ni Llanto ni Muerte", "Apocalipsis 21:4", "Y limpiará Dios toda lágrima de los ojos de ellos; y la muerte no será más; y no habrá más llanto, ni clamor, ni dolor: porque las primeras cosas son pasadas."),
            new Epiphany("rev_river_of_life", "Apocalipsis", 65, "Apocalipsis", 21, 0, 22, 1, MobEffects.WATER_BREATHING, 6000, 0, "Río de Agua de Vida", "Apocalipsis 22:1", "Después me mostró un río limpio de agua de vida, resplandeciente como cristal, que salía del trono de Dios y del Cordero.")

    );

    public static Optional<Epiphany> findByLocation(int bookIndex, int chapterIndex, int verseIndex) {
        return EPIPHANIES.stream()
                .filter(e -> e.bookIndex() == bookIndex && e.chapterIndex() == chapterIndex && e.verseIndex() == verseIndex)
                .findFirst();
    }

    public static List<Epiphany> findAllByChapter(int bookIndex, int chapterIndex) {
        return EPIPHANIES.stream()
                .filter(e -> e.bookIndex() == bookIndex && e.chapterIndex() == chapterIndex)
                .toList();
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
