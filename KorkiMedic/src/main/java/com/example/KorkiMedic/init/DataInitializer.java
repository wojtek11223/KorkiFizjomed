package com.example.KorkiMedic.init;

import com.example.KorkiMedic.entity.*;
import com.example.KorkiMedic.enums.Role;
import com.example.KorkiMedic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final PointActionRepository pointActionRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecializationRepository specializationRepository;
    private final AppointmentRepository appointmentRepository;
    private final AdRepository adRepository;

    private final ServRewardRepository servRewardRepository;

    private final ServiceRepository servRepository;
    @Autowired
    public DataInitializer(RewardRepository rewardRepository, UserRepository userRepository, PointActionRepository pointActionRepository, PasswordEncoder passwordEncoder, SpecializationRepository specializationRepository, ServiceRepository serviceRepository, AppointmentRepository appointmentRepository, AdRepository adRepository, ServRewardRepository servRewardRepository, ServiceRepository servRepository) {
        this.rewardRepository = rewardRepository;
        this.userRepository = userRepository;
        this.pointActionRepository = pointActionRepository;
        this.passwordEncoder = passwordEncoder;
        this.specializationRepository = specializationRepository;
        this.appointmentRepository = appointmentRepository;
        this.adRepository = adRepository;
        this.servRewardRepository = servRewardRepository;
        this.servRepository = servRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (adRepository.count() == 0) {
            Ad ad1 = new Ad();
            ad1.setTitle("Zdrowe żywienie");
            ad1.setDescription("Zdrowe żywienie jest fundamentem dobrego samopoczucia i zdrowia. W dzisiejszym świecie, w którym dostęp do różnorodnych produktów spożywczych jest nieograniczony, łatwo jest stracić orientację i zapomnieć o tym, co naprawdę wpływa na nasze zdrowie. Dieta ma ogromny wpływ na naszą kondycję fizyczną i psychiczną, a jej właściwy dobór może przyczynić się do długowieczności, zwiększonej energii oraz lepszej jakości życia. Co warto wiedzieć o zdrowym odżywianiu?\n" +
                    "\n" +
                    "Co to znaczy zdrowo się odżywiać?\n" +
                    "Zdrowe żywienie oznacza spożywanie pełnowartościowych produktów w odpowiednich ilościach, które dostarczają organizmowi wszystkich niezbędnych składników odżywczych. Chodzi tu o zachowanie równowagi między białkami, tłuszczami, węglowodanami, witaminami i minerałami, a także o unikanie nadmiaru soli, cukru i tłuszczów trans. Warto też dbać o odpowiednią ilość błonnika, który wspomaga procesy trawienne i pomaga w utrzymaniu zdrowia jelit.\n" +
                    "\n" +
                    "Zasady zdrowego żywienia\n" +
                    "Zróżnicowana dieta\n" +
                    "Aby zapewnić organizmowi wszystkie niezbędne składniki odżywcze, należy spożywać różnorodne produkty. Codziennie warto wprowadzać do diety różne grupy żywności, takie jak warzywa, owoce, pełnoziarniste produkty zbożowe, białka roślinne (np. rośliny strączkowe) oraz zdrowe tłuszcze (np. oleje roślinne, orzechy).\n" +
                    "\n" +
                    "Regularność posiłków\n" +
                    "Ważne jest, aby jeść regularnie – najlepiej 4-5 posiłków dziennie, w odstępach 3-4 godzinnych. Regularne posiłki pomagają utrzymać stały poziom energii przez cały dzień i zapobiegają napadom głodu, które mogą prowadzić do niezdrowych wyborów żywieniowych.\n" +
                    "\n" +
                    "Odpowiednia ilość wody\n" +
                    "Woda jest niezbędna do prawidłowego funkcjonowania organizmu. Dorośli powinni pić co najmniej 1,5-2 litry wody dziennie, a jej ilość może wzrosnąć w zależności od aktywności fizycznej czy warunków atmosferycznych. Woda wspomaga trawienie, reguluje temperaturę ciała i oczyszcza organizm z toksyn.\n" +
                    "\n" +
                    "Ograniczenie soli i cukru\n" +
                    "Zbyt duża ilość soli i cukru w diecie prowadzi do wielu problemów zdrowotnych, w tym nadciśnienia, otyłości, cukrzycy typu 2 oraz chorób serca. Zaleca się ograniczenie spożycia soli do około 5 gramów dziennie oraz unikanie nadmiernej ilości cukrów, szczególnie tych ukrytych w przetworzonych produktach.\n" +
                    "\n" +
                    "Zdrowe tłuszcze\n" +
                    "Nie wszystkie tłuszcze są złe. Ważne jest, aby wybierać tłuszcze roślinne (np. oliwa z oliwek, awokado, orzechy) oraz tłuszcze omega-3, które znajdują się w rybach, nasionach chia czy lnie. Tłuszcze zwierzęce, szczególnie te w nadmiarze, powinny być ograniczane, ponieważ przyczyniają się do rozwoju chorób serca i układu krążenia.\n" +
                    "\n" +
                    "Ograniczenie przetworzonych produktów\n" +
                    "Żywność przetworzona, bogata w konserwanty, sztuczne barwniki i aromaty, jest uboga w składniki odżywcze, a jej nadmiar w diecie może prowadzić do otyłości, cukrzycy i innych chorób cywilizacyjnych. Warto stawiać na świeże produkty i gotować samodzielnie, by mieć pełną kontrolę nad tym, co trafia do naszego organizmu.\n" +
                    "\n" +
                    "Co warto jeść, by dbać o zdrowie?\n" +
                    "Warzywa i owoce\n" +
                    "Warzywa i owoce to niezbędny element zdrowej diety. Są bogate w witaminy, minerały, antyoksydanty oraz błonnik. Należy dążyć do spożywania 5 porcji warzyw i owoców dziennie, starając się, by były one różnorodne pod względem kolorów i rodzajów.\n" +
                    "\n" +
                    "Pełnoziarniste produkty zbożowe\n" +
                    "\nChleb, kasze, ryż i makarony pełnoziarniste zawierają więcej błonnika i składników mineralnych niż ich białe odpowiedniki. Warto wybierać produkty, które zostały poddane minimalnej obróbce.\n" +
                    "\n" +
                    "Białka roślinne i zwierzęce\n" +
                    "\nBiałko jest niezbędne do budowy tkanek, mięśni i regeneracji organizmu. Dobrym źródłem białka są chude mięsa, ryby, jaja oraz rośliny strączkowe, tofu, orzechy czy nasiona.\n" +
                    "\n" +
                    "Tłuszcze roślinne\n" +
                    "\nTłuszcze zawarte w oliwie z oliwek, awokado, orzechach, nasionach czy rybach są nie tylko smaczne, ale także korzystne dla zdrowia. Są źródłem nienasyconych kwasów tłuszczowych, które mają działanie ochronne na serce i układ krążenia.\n" +
                    "\n" +
                    "Korzyści zdrowego żywienia\n" +
                    "\nLepsza kondycja fizyczna – zdrowa dieta dostarcza organizmowi wszystkich składników, które są niezbędne do utrzymania prawidłowego funkcjonowania ciała.\n" +
                    "Mniejsze ryzyko chorób przewlekłych – odpowiednie żywienie zmniejsza ryzyko otyłości, cukrzycy, nadciśnienia, chorób serca i innych poważnych schorzeń.\n" +
                    "Poprawa samopoczucia – dieta bogata w witaminy, minerały i błonnik wpływa pozytywnie na nasz nastrój, poziom energii oraz zdolności koncentracji.\n" +
                    "Długowieczność – zdrowe odżywianie ma ogromny wpływ na długość życia, zapobiegając przedwczesnemu starzeniu się organizmu.");
            ad1.setImage(loadImageAsBytes("jedzenie.jpg"));
            adRepository.save(ad1);

            Ad ad2 = new Ad();
            ad2.setTitle("Lecz depresje człowieku");
            ad2.setDescription("Depresja to jedno z najczęstszych zaburzeń psychicznych współczesnego świata. Szacuje się, że dotyczy ona milionów ludzi na całym świecie, niezależnie od wieku, płci czy statusu społecznego. Często jest to stan, który wykracza poza chwilowe obniżenie nastroju. Depresja to choroba, która może całkowicie zdominować życie, odbierając radość, energię i sens istnienia. Choć jest to trudne doświadczenie, warto pamiętać, że depresję można leczyć, a powrót do zdrowia jest możliwy.\n" +
                    "\n" +
                    "Czym jest depresja?\n" +
                    "Depresja to złożona choroba, która nie ogranicza się tylko do smutku czy chwilowego złego samopoczucia. Objawia się ona chronicznym obniżeniem nastroju, utratą zainteresowań, trudnościami w koncentracji, zmniejszoną energią, a także zaburzeniami snu i apetytem. Depresja może także prowadzić do myśli samobójczych, które są bardzo niebezpieczne. Często osoby borykające się z depresją czują się osamotnione, niezrozumiane i bezradne.\n" +
                    "\n" +
                    "Przyczyny depresji\n" +
                    "Przyczyny depresji są złożone i wieloaspektowe. Często wynikają z kombinacji czynników biologicznych, psychologicznych i społecznych. Do najczęstszych przyczyn depresji należą:\n" +
                    "\n" +
                    "Genetyka – skłonność do depresji może być dziedziczna, chociaż nie oznacza to, że każdy, kto ma w rodzinie osoby cierpiące na depresję, sam na nią zachoruje.\n" +
                    "Zaburzenia chemii mózgu – nierównowaga w poziomach neurotransmiterów (takich jak serotonina, noradrenalina czy dopamina) może wpływać na nastrój i samopoczucie.\n" +
                    "Czynniki życiowe – stresujące wydarzenia życiowe, takie jak utrata bliskiej osoby, rozwód czy problemy w pracy, mogą prowadzić do rozwoju depresji.\n" +
                    "Zaburzenia psychiczne – inne problemy, takie jak lęk czy zaburzenia obsesyjno-kompulsywne, mogą być powiązane z depresją.\n" +
                    "Społeczne izolowanie się – brak wsparcia ze strony rodziny i przyjaciół, samotność czy poczucie odrzucenia mogą pogłębiać objawy depresji.\n" +
                    "Objawy depresji – jak rozpoznać, że coś jest nie tak?\n" +
                    "Depresja może przybierać różne formy, a objawy mogą się różnić w zależności od osoby. Do najczęstszych symptomów depresji należą:\n" +
                    "\n" +
                    "Trwały smutek i przygnębienie, które nie mijają przez długi czas\n" +
                    "Utrata zainteresowań i radości z aktywności, które wcześniej sprawiały przyjemność\n" +
                    "Zmiany w apetycie i wadze – niektórzy tracą apetyt, inni jedzą więcej niż zwykle\n" +
                    "Problemy ze snem – bezsenność lub nadmierna senność\n" +
                    "Zmniejszona energia i uczucie zmęczenia, mimo odpoczynku\n" +
                    "Problemy z koncentracją i podejmowaniem decyzji\n" +
                    "Myśli samobójcze lub o śmierci\n" +
                    "Jeśli doświadczasz kilku z tych objawów przez dłuższy czas, warto skontaktować się z lekarzem lub terapeutą. Wczesne rozpoznanie depresji jest kluczowe dla skutecznego leczenia.\n" +
                    "\n" +
                    "Jak leczyć depresję?\n" +
                    "\nDepresja to choroba, którą można skutecznie leczyć, choć nie jest to proces szybki ani łatwy. Istnieje wiele metod terapeutycznych, które mogą pomóc w radzeniu sobie z tą chorobą:\n" +
                    "\n" +
                    "\nPsychoterapia – jednym z najskuteczniejszych sposobów leczenia depresji jest psychoterapia, szczególnie terapia poznawczo-behawioralna (CBT), która pomaga w identyfikowaniu i zmianie negatywnych myśli oraz przekonań. Inne formy terapii, jak terapia interpersonalna (IPT) czy psychodynamiczna, także mogą okazać się pomocne.\n" +
                    "\n" +
                    "\nLeki antydepresyjne – w wielu przypadkach lekarze przepisują leki, które pomagają przywrócić równowagę chemiczną w mózgu. Należy pamiętać, że leki należy przyjmować pod nadzorem specjalisty, ponieważ mogą mieć efekty uboczne, a ich skuteczność jest różna w zależności od osoby.\n" +
                    "\n" +
                    "\nWsparcie społeczne – rodzina i przyjaciele odgrywają niezwykle ważną rolę w procesie zdrowienia. Otoczenie, które oferuje zrozumienie i wsparcie, może pomóc w przełamaniu poczucia osamotnienia, które często towarzyszy depresji.\n" +
                    "\n" +
                    "\nAktywność fizyczna – regularne ćwiczenia fizyczne mogą znacząco poprawić nastrój, dzięki wydzielaniu endorfin, które są naturalnymi substancjami poprawiającymi samopoczucie. Nawet codzienne spacery czy joga mogą przynieść ulgę.\n" +
                    "\n" +
                    "\nZdrowa dieta i sen – odpowiednia ilość snu, zdrowa, zrównoważona dieta oraz unikanie używek, takich jak alkohol czy narkotyki, mają ogromne znaczenie w procesie leczenia depresji.\n" +
                    "\n" +
                    "\nTechniki relaksacyjne – medytacja, mindfulness czy głębokie oddychanie mogą pomóc w redukcji stresu, który często towarzyszy depresji.\n" +
                    "\n" +
                    "Dlaczego nie warto zwlekać?\n" +
                    "\nDepresja jest poważnym stanem, który może prowadzić do pogorszenia jakości życia, a w skrajnych przypadkach nawet do samobójstwa. Często osoby zmagające się z depresją nie szukają pomocy z powodu poczucia wstydu, winy lub przekonania, że \"muszą sobie poradzić same\". To błędne myślenie. Szukanie pomocy to oznaka siły, nie słabości.\n" +
                    "\n" +
                    "\nJeśli czujesz, że depresja zaczyna przejmować kontrolę nad Twoim życiem, nie czekaj. Skontaktuj się z profesjonalistą – lekarzem, terapeutą lub psychologiem, który pomoże Ci znaleźć odpowiednią drogę do zdrowienia.");
            ad2.setImage(loadImageAsBytes("depresja.jpg"));
            adRepository.save(ad2);

        }

        if (userRepository.count() == 0) {

            // Adding sample rewards
            rewardRepository.save(new Reward("Bon Zniżkowy", "50 zł zniżki", 50));
            rewardRepository.save(new Reward("Bezpłatna Wizyta Kontrolna", "Bezpłatna sesja kontrolna", 0));
            rewardRepository.save(new Reward("Pakiet Zdrowotny", "Kompleksowy pakiet badań zdrowotnych", 0));
            rewardRepository.save(new Reward("Konsultacja Dietetyczna", "Bezpłatna konsultacja z dietetykiem", 0));
            rewardRepository.save(new Reward("Masaż Relaksacyjny", "Bezpłatny masaż relaksacyjny", 0));

            // Roles
            Set<Role> adminRole = new HashSet<>();
            adminRole.add(Role.ADMIN);

            Set<Role> userRole = new HashSet<>();
            userRole.add(Role.USER);

            Set<Role> multipleRoles = new HashSet<>();
            multipleRoles.add(Role.USER);
            multipleRoles.add(Role.ADMIN);

            // Creating example users
            User user1 = new User(null, "Jan", "Kowalski", "jan@example.com", "123456789", LocalDate.of(1985, 5, 15), 0,
                    userRole, passwordEncoder.encode("qwerty"));
            User user2 = new User(null, "Anna", "Nowak", "anna.nowak@example.com", "987654321", LocalDate.of(1990, 8, 20), 0,
                    adminRole, passwordEncoder.encode("qwerty"));
            User user3 = new User(null, "Piotr", "Wiśniewski", "piotr.wisniewski@example.com", "555555555", LocalDate.of(1975, 12, 25), 0,
                    multipleRoles, passwordEncoder.encode("qwerty"));

            userRepository.saveAll(List.of(user1, user2, user3));

            // Specializations
            Specialization cardiology = new Specialization(null, "Cardiology");
            Specialization neurology = new Specialization(null, "Neurology");
            Specialization orthopedics = new Specialization(null, "Orthopedics");

            specializationRepository.saveAll(Set.of(cardiology, neurology, orthopedics));

            // Services
            Serv service1 = new Serv();
            service1.setName("Konsultacja Lekarska");
            service1.setDescription("Konsultacja z lekarzem specjalistą.");
            service1.setPrice(200);

            Serv service2 = new Serv();
            service2.setName("Badanie krwi");
            service2.setDescription("Kompleksowe badanie krwi.");
            service2.setPrice(100);

            Serv service3 = new Serv();
            service3.setName("USG");
            service3.setDescription("USG brzucha.");
            service3.setPrice(150);

            servRepository.save(service1);
            servRepository.save(service2);
            servRepository.save(service3);

            // Tworzenie przykładowych nagród
            Reward reward1 = new Reward();
            reward1.setName("Zniżka 10%");
            reward1.setDescription("Zniżka 10% na wybraną usługę.");
            reward1.setDiscount(0.1F);

            Reward reward2 = new Reward();
            reward2.setName("Zniżka 20%");
            reward2.setDescription("Zniżka 20% na wybraną usługę.");
            reward2.setDiscount(0.2F);

            Reward reward3 = new Reward();
            reward3.setName("Bezpłatne badanie krwi");
            reward3.setDescription("Bezpłatne wykonanie badania krwi.");
            reward3.setDiscount(0.0F);

            rewardRepository.save(reward1);
            rewardRepository.save(reward2);
            rewardRepository.save(reward3);

            // Tworzenie powiązań między usługami a nagrodami z obniżką
            ServReward servReward1 = new ServReward(service1, reward1, 10);
            ServReward servReward2 = new ServReward(service2, reward2, 20);
            ServReward servReward3 = new ServReward(service3, reward3, 100);

            servRewardRepository.save(servReward1);
            servRewardRepository.save(servReward2);
            servRewardRepository.save(servReward3);

            // Creating doctors with specializations and services
            User doctor1 = new User(null, "John", "Doe", "john@example.com", "123456788", LocalDate.of(1980, 5, 15), 0,
                    Set.of(Role.DOCTOR,Role.USER), Set.of(cardiology, neurology), Set.of(service1, service2),null, null, null, passwordEncoder.encode("qwerty"));
            User doctor2 = new User(null, "Jane", "Smith", "jane.smith@example.com", "987654389", LocalDate.of(1975, 3, 25), 0,
                    Set.of(Role.DOCTOR,Role.USER), Set.of(orthopedics), Set.of(service1, service3),null, null, null, passwordEncoder.encode("qwerty"));

            userRepository.saveAll(List.of(doctor1, doctor2));

            if (appointmentRepository.count() == 0) {
                User patient = userRepository.findByEmail("jan@example.com").orElseThrow(() -> new RuntimeException("User not found"));

                // Utwórz obiekt Calendar ustawiony na dzisiejszą datę
                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR, 16);
                calendar.set(Calendar.MINUTE, 15);
                calendar.set(Calendar.SECOND, 0);

                Date tomorrowDate = calendar.getTime();

                Appointment appointment1 = new Appointment(null, patient, doctor1, service1, tomorrowDate,null,100, "Brak notatki", "Zatwierdzona", null, null);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR, 13);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                tomorrowDate = calendar.getTime();
                Appointment appointment2 = new Appointment(null, patient, doctor2, service2, tomorrowDate,null, 550, "Brak notatki", "Anulowana", null, null);
                appointmentRepository.saveAll(List.of(appointment1, appointment2));
                adminRole = new HashSet<>();
                adminRole.add(Role.ADMIN);

               userRole = new HashSet<>();
                userRole.add(Role.USER);

                multipleRoles = new HashSet<>();
                multipleRoles.add(Role.USER);
                multipleRoles.add(Role.ADMIN);

                // Tworzenie dodatkowych użytkowników
                User user4 = new User(null, "Katarzyna", "Wiśniewska", "katarzyna.wisniewska@example.com", "777777777", LocalDate.of(1995, 1, 10), 0,
                        userRole, passwordEncoder.encode("haslo123"));
                User user5 = new User(null, "Marek", "Zieliński", "marek.zielinski@example.com", "444444444", LocalDate.of(1980, 11, 5), 0,
                        adminRole, passwordEncoder.encode("haslo456"));
                User user6 = new User(null, "Paulina", "Dąbrowska", "paulina.dabrowska@example.com", "666666666", LocalDate.of(1992, 6, 18), 0,
                        multipleRoles, passwordEncoder.encode("haslo789"));

                userRepository.saveAll(List.of(user4, user5, user6));

                // Dodatkowe specjalizacje
                Specialization dermatology = new Specialization(null, "Dermatologia");
                Specialization pediatrics = new Specialization(null, "Pediatria");
                Specialization psychiatry = new Specialization(null, "Psychiatria");

                specializationRepository.saveAll(Set.of(dermatology, pediatrics, psychiatry));

                // Dodatkowe usługi
                Serv service4 = new Serv();
                service4.setName("Konsultacja Dermatologiczna");
                service4.setDescription("Konsultacja z dermatologiem.");
                service4.setPrice(180);

                Serv service5 = new Serv();
                service5.setName("Terapia Psychologiczna");
                service5.setDescription("Indywidualna sesja terapeutyczna.");
                service5.setPrice(220);

                Serv service6 = new Serv();
                service6.setName("Rehabilitacja");
                service6.setDescription("Zabieg rehabilitacyjny.");
                service6.setPrice(250);

                servRepository.save(service4);
                servRepository.save(service5);
                servRepository.save(service6);

                // Nowe nagrody
                Reward reward4 = new Reward();
                reward4.setName("Konsultacja dietetyczna");
                reward4.setDescription("Bezpłatna konsultacja dietetyczna.");
                reward4.setDiscount(0);

                Reward reward5 = new Reward();
                reward5.setName("Bezpłatne USG");
                reward5.setDescription("Bezpłatne wykonanie USG.");
                reward5.setDiscount(0);

                rewardRepository.save(reward4);
                rewardRepository.save(reward5);

                // Nowe powiązania między usługami a nagrodami z obniżką
                ServReward servReward4 = new ServReward(service4, reward4, 50); // 50% zniżki na Konsultację Dermatologiczną
                ServReward servReward5 = new ServReward(service6, reward5, 100); // 100% zniżki (darmowe) na Rehabilitację

                servRewardRepository.save(servReward4);
                servRewardRepository.save(servReward5);

                // Dodanie nowych lekarzy ze specjalizacjami i usługami
                User doctor3 = new User(null, "Agnieszka", "Kwiatkowska", "agnieszka.kwiatkowska@example.com", "888888888", LocalDate.of(1982, 4, 5), 0,
                        Set.of(Role.DOCTOR,Role.USER), Set.of(dermatology), Set.of(service4),null, null, null, passwordEncoder.encode("haslo123"));
                User doctor4 = new User(null, "Tomasz", "Lewandowski", "tomasz.lewandowski@example.com", "999999999", LocalDate.of(1978, 9, 20), 0,
                        Set.of(Role.DOCTOR,Role.USER), Set.of(pediatrics, psychiatry), Set.of(service5),null, null, null, passwordEncoder.encode("haslo123"));

                userRepository.saveAll(List.of(doctor3, doctor4));

                // Tworzenie przykładowych wizyt
                patient = userRepository.findByEmail("jan@example.com").orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

                calendar = Calendar.getInstance();

                // Wizyta za dwa dni
                calendar.add(Calendar.DAY_OF_YEAR, 2);
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 30);
                calendar.set(Calendar.SECOND, 0);

                Date futureDate = calendar.getTime();

                Appointment appointment3 = new Appointment(null, patient, doctor3, service4, futureDate,null,300, "Konsultacja dermatologiczna","Niezatwierdzony", null, null);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                futureDate = calendar.getTime();
                Appointment appointment4 = new Appointment(null, patient, doctor4, service5, futureDate,null,200, "Terapia psychologiczna", "Anulowany" ,null, null);
                appointmentRepository.saveAll(List.of(appointment3, appointment4));
            }

            if (pointActionRepository.count() == 0) {
                // Adding default point actions
                List<PointAction> defaultActions = List.of(
                        new PointAction(null, "Za rejestrację w systemie", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null,  "Za realizacje wizyty", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null,  "Zwrot punktów", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null,  "Za polecenie nowego pacjenta", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null,  "Za wymianę punktów na nagrodę", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null, "Codzienne logowanie", LocalDateTime.now(), LocalDateTime.now())
                );

                pointActionRepository.saveAll(defaultActions);
                System.out.println("Dodano domyślne akcje punktowe.");
            } else {
                System.out.println("Akcje punktowe już istnieją.");
            }
        }


    }

    private byte[] loadImageAsBytes(String resourcePath) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            java.net.URL resourceUrl = classLoader.getResource(resourcePath);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Plik nie został znaleziony: " + resourcePath);
            }
            return java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(resourceUrl.toURI()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
