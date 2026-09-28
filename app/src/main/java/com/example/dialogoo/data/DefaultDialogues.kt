package com.example.dialogoo.data

import com.example.dialogoo.model.Dialogue
import com.example.dialogoo.model.DialogueLine
import com.example.dialogoo.model.Level
import com.example.dialogoo.model.VocabularyItem

object DefaultDialogues {
    val list: List<Dialogue> = listOf(
        Dialogue(
            id = "lesson-coffee-shop",
            title = "Ordering at the Coffee Shop",
            situation = "Pedindo seu café da manhã favorito e personalizando os ingredientes no balcão.",
            level = Level.A1,
            order = 1,
            coverDrawableRes = "cover_coffee_shop",
            lines = listOf(
                DialogueLine(
                    id = "cs-1",
                    speaker = "Barista",
                    text = "Good morning! What can I get started for you today?",
                    translation = "Bom dia! O que posso preparar para você hoje?",
                    pronunciationGuide = "gud mór-ning! uát kén ai guét stár-ted for iu tu-dei?",
                    keyVocabulary = listOf(
                        VocabularyItem("Good morning", "Bom dia"),
                        VocabularyItem("get started", "começar / preparar")
                    )
                ),
                DialogueLine(
                    id = "cs-2",
                    speaker = "You (Student)",
                    text = "Hi! I would like a medium iced latte with oat milk, please.",
                    translation = "Oi! Eu gostaria de um café latte gelado médio com leite de aveia, por favor.",
                    pronunciationGuide = "hai! ai uúd láik a mí-di-um áist lá-tei uiz out milk, pliz.",
                    keyVocabulary = listOf(
                        VocabularyItem("would like", "gostaria"),
                        VocabularyItem("oat milk", "leite de aveia")
                    )
                ),
                DialogueLine(
                    id = "cs-3",
                    speaker = "Barista",
                    text = "Sure thing! Do you want any vanilla syrup or sugar in that?",
                    translation = "Com certeza! Você quer xarope de baunilha ou açúcar?",
                    pronunciationGuide = "chur zing! du iu uónt é-ni va-ní-la sí-rap or chú-gar in dét?",
                    keyVocabulary = listOf(
                        VocabularyItem("Sure thing", "Com certeza"),
                        VocabularyItem("syrup", "xarope / calda")
                    )
                ),
                DialogueLine(
                    id = "cs-4",
                    speaker = "You (Student)",
                    text = "Just one pump of vanilla, please. And could I also get a blueberry muffin?",
                    translation = "Apenas uma dose de baunilha, por favor. E eu poderia levar também um muffin de mirtilo?",
                    pronunciationGuide = "djast uan pamp ov va-ní-la, pliz. end kúd ai ól-so guét a blú-bé-ri má-fin?",
                    keyVocabulary = listOf(
                        VocabularyItem("pump", "dose / borrifada"),
                        VocabularyItem("could I get", "eu poderia pegar/levar")
                    )
                ),
                DialogueLine(
                    id = "cs-5",
                    speaker = "Barista",
                    text = "Absolutely. That will be six dollars and fifty cents. Cash or card?",
                    translation = "Com certeza. Fica seis dólares e cinquenta centavos. Dinheiro ou cartão?",
                    pronunciationGuide = "éb-so-lút-li. dét uil bi siks dó-lars end fíf-ti sents. késh or kard?",
                    keyVocabulary = listOf(
                        VocabularyItem("Absolutely", "Com certeza / Absolutamente"),
                        VocabularyItem("Cash or card", "Dinheiro ou cartão")
                    )
                ),
                DialogueLine(
                    id = "cs-6",
                    speaker = "You (Student)",
                    text = "I will pay with card via contactless. Here you go. Thank you so much!",
                    translation = "Vou pagar com cartão por aproximação. Aqui está. Muito obrigado!",
                    pronunciationGuide = "ai uil pei uiz kard vaia kón-tekt-les. hir iu gou. zénk iu sou mátch!",
                    keyVocabulary = listOf(
                        VocabularyItem("contactless", "aproximação"),
                        VocabularyItem("Here you go", "Aqui está")
                    )
                )
            )
        ),
        Dialogue(
            id = "lesson-first-day-work",
            title = "First Day at the Office",
            situation = "Conhecendo a equipe e o seu novo gerente na recepção da empresa.",
            level = Level.A1,
            order = 2,
            coverDrawableRes = "cover_first_day_work",
            lines = listOf(
                DialogueLine(
                    id = "fd-1",
                    speaker = "Receptionist",
                    text = "Welcome to Vertex Tech! Are you our new mobile developer?",
                    translation = "Bem-vindo à Vertex Tech! Você é nosso novo desenvolvedor mobile?",
                    pronunciationGuide = "uél-kam tu vér-teks ték! ar iu áu-ar niu móu-bil de-vé-lo-per?",
                    keyVocabulary = listOf(
                        VocabularyItem("Welcome", "Bem-vindo"),
                        VocabularyItem("developer", "desenvolvedor")
                    )
                ),
                DialogueLine(
                    id = "fd-2",
                    speaker = "You (Student)",
                    text = "Yes, nice to meet you! My name is Alex and today is my first day.",
                    translation = "Sim, prazer em conhecê-lo! Meu nome é Alex e hoje é meu primeiro dia.",
                    pronunciationGuide = "iés, náis tu mit iu! mai neim iz á-leks end tu-dei iz mai först dei.",
                    keyVocabulary = listOf(
                        VocabularyItem("nice to meet you", "prazer em conhecê-lo"),
                        VocabularyItem("first day", "primeiro dia")
                    )
                ),
                DialogueLine(
                    id = "fd-3",
                    speaker = "Manager",
                    text = "Alex! Great to have you on board. Let me show you your desk and workstation.",
                    translation = "Alex! Ótimo ter você a bordo. Deixe-me mostrar sua mesa e estação de trabalho.",
                    pronunciationGuide = "á-leks! greit tu rév iu on bord. lét mi chou iu iór désk end uörk-stei-chan.",
                    keyVocabulary = listOf(
                        VocabularyItem("on board", "a bordo / no time"),
                        VocabularyItem("workstation", "estação de trabalho")
                    )
                ),
                DialogueLine(
                    id = "fd-4",
                    speaker = "You (Student)",
                    text = "Thank you so much! I am very excited to meet the engineering team.",
                    translation = "Muito obrigado! Estou muito animado para conhecer o time de engenharia.",
                    pronunciationGuide = "zénk iu sou mátch! ai ém vé-ri ék-sái-ted tu mit di én-dji-ní-ring tim.",
                    keyVocabulary = listOf(
                        VocabularyItem("excited", "empolgado / animado"),
                        VocabularyItem("engineering team", "equipe de engenharia")
                    )
                )
            )
        ),
        Dialogue(
            id = "lesson-grocery-store",
            title = "At the Grocery Store",
            situation = "Perguntando a um funcionário do mercado onde encontrar ingredientes especiais.",
            level = Level.A1,
            order = 3,
            coverDrawableRes = "cover_grocery_store",
            lines = listOf(
                DialogueLine(
                    id = "gs-1",
                    speaker = "You (Student)",
                    text = "Excuse me, where can I find olive oil and organic tomatoes?",
                    translation = "Com licença, onde posso encontrar azeite de oliva e tomates orgânicos?",
                    pronunciationGuide = "éks-kiuz mi, uér kén ai fáind ó-liv óil end or-gé-nik to-méi-tous?",
                    keyVocabulary = listOf(
                        VocabularyItem("Excuse me", "Com licença"),
                        VocabularyItem("olive oil", "azeite de oliva")
                    )
                ),
                DialogueLine(
                    id = "gs-2",
                    speaker = "Staff",
                    text = "Olive oil is in aisle four, right next to the pasta sauces.",
                    translation = "O azeite está no corredor quatro, bem ao lado dos molhos de macarrão.",
                    pronunciationGuide = "ó-liv óil iz in áil fór, ráit nékst tu da pás-ta só-sez.",
                    keyVocabulary = listOf(
                        VocabularyItem("aisle", "corredor"),
                        VocabularyItem("next to", "ao lado de")
                    )
                ),
                DialogueLine(
                    id = "gs-3",
                    speaker = "You (Student)",
                    text = "Perfect! And are these avocados on sale this week?",
                    translation = "Perfeito! E esses abacates estão em promoção esta semana?",
                    pronunciationGuide = "pör-fekt! end ar diz a-vo-ká-dous on seil dis uik?",
                    keyVocabulary = listOf(
                        VocabularyItem("on sale", "em promoção"),
                        VocabularyItem("avocados", "abacates")
                    )
                ),
                DialogueLine(
                    id = "gs-4",
                    speaker = "Staff",
                    text = "Yes, buy two get one free until Sunday!",
                    translation = "Sim, compre dois e leve um grátis até domingo!",
                    pronunciationGuide = "iés, bai tu guét uan fri an-tíl sán-dei!",
                    keyVocabulary = listOf(
                        VocabularyItem("buy two get one free", "compre dois leve um")
                    )
                )
            )
        ),
        Dialogue(
            id = "lesson-city-directions",
            title = "Asking for City Directions",
            situation = "Encontrando a estação de metrô mais próxima no centro de Nova York.",
            level = Level.A2,
            order = 1,
            coverDrawableRes = "cover_city_directions",
            lines = listOf(
                DialogueLine(
                    id = "cd-1",
                    speaker = "You (Student)",
                    text = "Pardon me, could you point me toward the nearest subway station?",
                    translation = "Perdão, você poderia me apontar a estação de metrô mais próxima?",
                    pronunciationGuide = "pár-don mi, kúd iu póint mi tu-uórd da ní-rest sáb-uei stei-chan?",
                    keyVocabulary = listOf(
                        VocabularyItem("nearest", "mais próximo"),
                        VocabularyItem("subway station", "estação de metrô")
                    )
                ),
                DialogueLine(
                    id = "cd-2",
                    speaker = "Local Citizen",
                    text = "Walk straight for two blocks, then turn left at the traffic light on Grand Avenue.",
                    translation = "Caminhe reto por dois quarteirões, depois vire à esquerda no semáforo na Grand Avenue.",
                    pronunciationGuide = "uók streit for tu blóks, den törn léft ét da tré-fik láit on grend é-ve-niu.",
                    keyVocabulary = listOf(
                        VocabularyItem("walk straight", "caminhar reto"),
                        VocabularyItem("traffic light", "semáforo")
                    )
                ),
                DialogueLine(
                    id = "cd-3",
                    speaker = "You (Student)",
                    text = "How long does it take on foot? Is it within walking distance?",
                    translation = "Quanto tempo leva a pé? É perto para ir andando?",
                    pronunciationGuide = "ráu lóng dáz it teik on fut? iz it ui-zín uó-king dís-tens?",
                    keyVocabulary = listOf(
                        VocabularyItem("on foot", "a pé"),
                        VocabularyItem("walking distance", "distância caminhável")
                    )
                ),
                DialogueLine(
                    id = "cd-4",
                    speaker = "Local Citizen",
                    text = "It's only about a five-minute walk. You can't miss the blue transit sign.",
                    translation = "É apenas uns cinco minutos a pé. Não tem erro com a placa azul do transporte.",
                    pronunciationGuide = "its óun-li a-báut a faiv-mí-nat uók. iu kent mis da blu trén-zit sain.",
                    keyVocabulary = listOf(
                        VocabularyItem("You can't miss it", "Não tem erro / impossível errar")
                    )
                )
            )
        ),
        Dialogue(
            id = "lesson-restaurant-order",
            title = "Dinner Order at the Restaurant",
            situation = "Fazendo pedidos de pratos especiais e consultando o garçom sobre alergias.",
            level = Level.A2,
            order = 2,
            coverDrawableRes = "cover_restaurant_order",
            lines = listOf(
                DialogueLine(
                    id = "ro-1",
                    speaker = "Waiter",
                    text = "Good evening! Have you had a chance to look over our dinner specials?",
                    translation = "Boa noite! Já tiveram a chance de dar uma olhada nos nossos pratos especiais?",
                    pronunciationGuide = "gud ív-ning! rév iu réd a tchéns tu luk óu-ver áu-ar dín-ner spé-chols?",
                    keyVocabulary = listOf(
                        VocabularyItem("look over", "examinar / dar uma olhada"),
                        VocabularyItem("specials", "pratos do dia / especiais")
                    )
                ),
                DialogueLine(
                    id = "ro-2",
                    speaker = "You (Student)",
                    text = "Yes! I would like the grilled salmon, but does it contain any gluten or dairy?",
                    translation = "Sim! Eu gostaria do salmão grelhado, mas ele contém algum glúten ou laticínio?",
                    pronunciationGuide = "iés! ai uúd láik da grild sé-mon, bat dáz it kon-téin é-ni glú-ten or dé-ri?",
                    keyVocabulary = listOf(
                        VocabularyItem("grilled salmon", "salmão grelhado"),
                        VocabularyItem("dairy", "laticínios")
                    )
                ),
                DialogueLine(
                    id = "ro-3",
                    speaker = "Waiter",
                    text = "The salmon is seasoned with herbs and olive oil, entirely gluten-free and dairy-free.",
                    translation = "O salmão é temperado com ervas e azeite, totalmente sem glúten e sem leite.",
                    pronunciationGuide = "da sé-mon iz sí-zond uiz örbs end ó-liv óil, en-tái-er-li glú-ten-fri end dé-ri-fri.",
                    keyVocabulary = listOf(
                        VocabularyItem("seasoned", "temperado"),
                        VocabularyItem("entirely", "totalmente / inteiramente")
                    )
                ),
                DialogueLine(
                    id = "ro-4",
                    speaker = "You (Student)",
                    text = "Wonderful! I'll take that with roasted vegetables and a sparkling water with lime.",
                    translation = "Maravilhoso! Vou querer esse com legumes assados e uma água com gás com limão.",
                    pronunciationGuide = "uán-der-ful! ail teik dét uiz róus-ted védj-te-bols end a spár-kling uó-ter uiz láim.",
                    keyVocabulary = listOf(
                        VocabularyItem("roasted vegetables", "legumes assados"),
                        VocabularyItem("sparkling water", "água com gás")
                    )
                )
            )
        ),
        Dialogue(
            id = "lesson-doctor-office",
            title = "Doctor's Consultation",
            situation = "Explicando sintomas de dor de cabeça e febre ao médico na clínica.",
            level = Level.B1,
            order = 1,
            coverDrawableRes = "cover_doctor_office",
            lines = listOf(
                DialogueLine(
                    id = "do-1",
                    speaker = "Dr. Miller",
                    text = "Hello! Come on in. What seems to be bothering you today?",
                    translation = "Olá! Entre. O que parece estar incomodando você hoje?",
                    pronunciationGuide = "he-lóu! kam on in. uát sims tu bi bó-ze-ring iu tu-dei?",
                    keyVocabulary = listOf(
                        VocabularyItem("Come on in", "Pode entrar"),
                        VocabularyItem("bothering", "incomodando")
                    )
                ),
                DialogueLine(
                    id = "do-2",
                    speaker = "You (Student)",
                    text = "I have had a throbbing headache and a persistent mild fever for three days.",
                    translation = "Estou com uma dor de cabeça latejante e uma febre leve persistente há três dias.",
                    pronunciationGuide = "ai rév réd a zró-bing héd-eik end a per-sís-tent máild fí-ver for zri deis.",
                    keyVocabulary = listOf(
                        VocabularyItem("throbbing headache", "dor de cabeça latejante"),
                        VocabularyItem("persistent", "persistente")
                    )
                ),
                DialogueLine(
                    id = "do-3",
                    speaker = "Dr. Miller",
                    text = "Let's check your temperature and blood pressure. Have you experienced any dizziness?",
                    translation = "Vamos verificar sua temperatura e pressão arterial. Sentiu alguma tontura?",
                    pronunciationGuide = "léts tchék iór tém-pra-tchar end blád pré-char. rév iu éks-pí-ri-enst é-ni dí-zi-nes?",
                    keyVocabulary = listOf(
                        VocabularyItem("blood pressure", "pressão arterial"),
                        VocabularyItem("dizziness", "tontura")
                    )
                ),
                DialogueLine(
                    id = "do-4",
                    speaker = "You (Student)",
                    text = "Only when standing up quickly. I've been staying hydrated, but it hasn't improved much.",
                    translation = "Só ao levantar rápido. Tenho me mantido hidratado, mas não melhorou muito.",
                    pronunciationGuide = "óun-li uén stén-ding áp kuík-li. áiv bin stéi-ing hai-dréi-ted, bat it ré-zent im-prúvd mátch.",
                    keyVocabulary = listOf(
                        VocabularyItem("standing up", "levantar-se"),
                        VocabularyItem("hydrated", "hidratado")
                    )
                )
            )
        ),
        Dialogue(
            id = "lesson-job-interview",
            title = "Job Interview for Mobile Developer",
            situation = "Falando sobre sua experiência com Android, Compose e arquitetura de software.",
            level = Level.B1,
            order = 2,
            coverDrawableRes = "cover_job_interview",
            lines = listOf(
                DialogueLine(
                    id = "ji-1",
                    speaker = "Interviewer",
                    text = "Alex, could you walk us through how you manage UI state in Jetpack Compose?",
                    translation = "Alex, você poderia nos explicar como gerencia o estado de interface no Jetpack Compose?",
                    pronunciationGuide = "á-leks, kúd iu uók as zru ráu iu mé-nidj iu-ái steit in djét-pék kom-póuz?",
                    keyVocabulary = listOf(
                        VocabularyItem("walk through", "explicar passo a passo"),
                        VocabularyItem("UI state", "estado da interface")
                    )
                ),
                DialogueLine(
                    id = "ji-2",
                    speaker = "You (Student)",
                    text = "I prefer using StateFlow inside ViewModels and collecting it with lifecycle awareness.",
                    translation = "Eu prefiro usar StateFlow dentro dos ViewModels e coletá-lo respeitando o ciclo de vida.",
                    pronunciationGuide = "ai pri-fër iú-zing steit-flóu in-sáid viu-mó-dels end ko-lék-ting it uiz láif-sái-kol a-uér-nes.",
                    keyVocabulary = listOf(
                        VocabularyItem("lifecycle awareness", "consciência do ciclo de vida"),
                        VocabularyItem("collecting", "coletando / observando")
                    )
                ),
                DialogueLine(
                    id = "ji-3",
                    speaker = "Interviewer",
                    text = "Impressive. And how do you ensure high performance during list recompositions?",
                    translation = "Impressionante. E como você garante alto desempenho durante recomposições de listas?",
                    pronunciationGuide = "im-pré-siv. end ráu du iu en-chúr hai per-fór-mens diú-ring list ri-kom-po-zí-chans?",
                    keyVocabulary = listOf(
                        VocabularyItem("ensure", "garantir"),
                        VocabularyItem("recomposition", "recomposição")
                    )
                ),
                DialogueLine(
                    id = "ji-4",
                    speaker = "You (Student)",
                    text = "I provide stable keys in LazyColumn and memoize expensive computations with derivedStateOf.",
                    translation = "Forneço chaves estáveis no LazyColumn e memorizo cálculos pesados com derivedStateOf.",
                    pronunciationGuide = "ai pro-váid stéi-bol kiz in léi-zi-kó-lam end me-mo-áiz eks-pén-siv kom-piu-téi-chans uiz di-ráivd-steit-ov.",
                    keyVocabulary = listOf(
                        VocabularyItem("stable keys", "chaves estáveis"),
                        VocabularyItem("expensive computations", "cálculos pesados")
                    )
                )
            )
        ),
        Dialogue(
            id = "lesson-travel-conversation",
            title = "Airport Gate and Boarding",
            situation = "Tirando dúvidas sobre conexão de voo e bagagem de mão no portão de embarque.",
            level = Level.B1,
            order = 3,
            coverDrawableRes = "cover_travel_conversation",
            lines = listOf(
                DialogueLine(
                    id = "tc-1",
                    speaker = "You (Student)",
                    text = "Good afternoon, is this flight 402 with connecting service to London Heathrow?",
                    translation = "Boa tarde, este é o voo 402 com conexão para Londres Heathrow?",
                    pronunciationGuide = "gud éf-ter-nun, iz dis fláit for-ou-tu uiz ko-nék-ting sör-vis tu lán-don hí-zrou?",
                    keyVocabulary = listOf(
                        VocabularyItem("connecting service", "voo de conexão"),
                        VocabularyItem("Heathrow", "Heathrow (aeroporto)")
                    )
                ),
                DialogueLine(
                    id = "tc-2",
                    speaker = "Gate Agent",
                    text = "Yes, sir! Boarding will commence in approximately fifteen minutes for Zone 1.",
                    translation = "Sim, senhor! O embarque começará em aproximadamente quinze minutos para a Zona 1.",
                    pronunciationGuide = "iés, sör! bór-ding uil ko-méns in a-prók-si-met-li fíf-tin mí-nats for zoun uan.",
                    keyVocabulary = listOf(
                        VocabularyItem("commence", "começar / ter início"),
                        VocabularyItem("boarding", "embarque")
                    )
                ),
                DialogueLine(
                    id = "tc-3",
                    speaker = "You (Student)",
                    text = "Will my checked baggage be automatically transferred to the connecting carrier?",
                    translation = "Minha bagagem despachada será transferida automaticamente para a companhia de conexão?",
                    pronunciationGuide = "uil mai tchékd bé-guidj bi ó-to-mé-tik-li tréns-fërd tu da ko-nék-ting ké-ri-er?",
                    keyVocabulary = listOf(
                        VocabularyItem("checked baggage", "bagagem despachada"),
                        VocabularyItem("carrier", "companhia aérea")
                    )
                ),
                DialogueLine(
                    id = "tc-4",
                    speaker = "Gate Agent",
                    text = "All through to London. You only need to claim it at your final destination.",
                    translation = "Direto para Londres. Você só precisa retirá-la no seu destino final.",
                    pronunciationGuide = "ól zru tu lán-don. iu óun-li nid tu kleim it ét iór fái-nol des-ti-néi-chan.",
                    keyVocabulary = listOf(
                        VocabularyItem("claim baggage", "retirar a bagagem")
                    )
                )
            )
        ),
        Dialogue(
            id = "lesson-business-negotiation",
            title = "Contract & Partnership Negotiation",
            situation = "Negociando termos de contrato, cláusulas de SLA e prazos de entrega de software.",
            level = Level.B2,
            order = 1,
            coverDrawableRes = "cover_business_negotiation",
            lines = listOf(
                DialogueLine(
                    id = "bn-1",
                    speaker = "Partner Executive",
                    text = "We reviewed your software proposal, but our legal team has reservations about the liability clause.",
                    translation = "Analisamos sua proposta de software, mas nosso departamento jurídico tem ressalvas sobre a cláusula de responsabilidade.",
                    pronunciationGuide = "ui ri-viúd iór sóft-uér pro-póu-zol, bat áu-ar lí-gol tim réz re-zer-véi-chans a-báut da lai-a-bí-li-ti klóz.",
                    keyVocabulary = listOf(
                        VocabularyItem("reservations", "ressalvas"),
                        VocabularyItem("liability clause", "cláusula de responsabilidade")
                    )
                ),
                DialogueLine(
                    id = "bn-2",
                    speaker = "You (Student)",
                    text = "We are open to capping our liability to the annual contract value to alleviate your concern.",
                    translation = "Estamos dispostos a limitar nossa responsabilidade ao valor anual do contrato para aliviar sua preocupação.",
                    pronunciationGuide = "ui ar óu-pen tu ké-ping áu-ar lai-a-bí-li-ti tu di é-niu-al kón-trekt vé-liu tu a-lí-vi-eit iór kon-sörn.",
                    keyVocabulary = listOf(
                        VocabularyItem("capping", "limitar com teto"),
                        VocabularyItem("alleviate", "aliviar / mitigar")
                    )
                ),
                DialogueLine(
                    id = "bn-3",
                    speaker = "Partner Executive",
                    text = "That demonstrates great flexibility. Could we also extend the support window to 24/7 coverage?",
                    translation = "Isso demonstra grande flexibilidade. Poderíamos também estender a janela de suporte para cobertura 24/7?",
                    pronunciationGuide = "dét dé-mon-streits greit flek-si-bí-li-ti. kúd ui ól-so éks-ténd da sa-pórt uín-dou tu tuén-ti-for-sé-ven kó-ve-ridj?",
                    keyVocabulary = listOf(
                        VocabularyItem("flexibility", "flexibilidade"),
                        VocabularyItem("support window", "janela de suporte")
                    )
                ),
                DialogueLine(
                    id = "bn-4",
                    speaker = "You (Student)",
                    text = "Yes, we can bundle round-the-clock priority support into tier two with a mutual three-year commitment.",
                    translation = "Sim, podemos incluir suporte prioritário ininterrupto no plano dois com um compromisso mútuo de três anos.",
                    pronunciationGuide = "iés, ui kén bán-dol ráund-da-klók prai-ó-ri-ti sa-pórt ín-tu tí-er tu uiz a miú-tchu-al zri-íer ko-mít-ment.",
                    keyVocabulary = listOf(
                        VocabularyItem("round-the-clock", "24 horas / ininterrupto"),
                        VocabularyItem("commitment", "compromisso")
                    )
                )
            )
        ),
        Dialogue(
            id = "lesson-academic-debate",
            title = "Academic Debate: AI Ethics",
            situation = "Defendendo argumentos sobre governança e transparência de inteligência artificial.",
            level = Level.C1,
            order = 1,
            coverDrawableRes = "cover_academic_debate",
            lines = listOf(
                DialogueLine(
                    id = "ad-1",
                    speaker = "Professor Hayes",
                    text = "Do you believe proprietary alignment frameworks are sufficient to prevent catastrophic misuse?",
                    translation = "Você acredita que estruturas proprietárias de alinhamento sejam suficientes para prevenir mau uso catastrófico?",
                    pronunciationGuide = "du iu bi-lív pro-prái-e-te-ri a-láin-ment fréim-uörks ar sa-fí-chent tu pri-vént ka-tes-tró-fik mis-iús?",
                    keyVocabulary = listOf(
                        VocabularyItem("alignment frameworks", "estruturas de alinhamento"),
                        VocabularyItem("misuse", "mau uso / uso indevido")
                    )
                ),
                DialogueLine(
                    id = "ad-2",
                    speaker = "You (Student)",
                    text = "Relying strictly on self-regulation is inherently precarious; public oversight and open benchmarks are indispensable.",
                    translation = "Depender estritamente de autorregulação é intrinsecamente precário; supervisão pública e benchmarks abertos são indispensáveis.",
                    pronunciationGuide = "ri-lái-ing stríkt-li on self-reg-iu-léi-chan iz in-hí-rent-li pri-ké-ri-as; páb-lik óu-ver-sait end óu-pen bénch-marks ar in-dis-pén-sa-bol.",
                    keyVocabulary = listOf(
                        VocabularyItem("self-regulation", "autorregulação"),
                        VocabularyItem("indispensable", "indispensável")
                    )
                ),
                DialogueLine(
                    id = "ad-3",
                    speaker = "Professor Hayes",
                    text = "Yet imposing onerous compliance mandates might inadvertently stifle nascent research labs.",
                    translation = "No entanto, impor exigências de conformidade onerosas pode, inadvertidamente, sufocar laboratórios de pesquisa nascentes.",
                    pronunciationGuide = "iét im-póu-zing ó-ne-ras kom-plái-ens mén-deits máit in-ed-vër-tent-li stái-fol né-sent ri-sörtch lébs.",
                    keyVocabulary = listOf(
                        VocabularyItem("onerous mandates", "mandatos/exigências onerosas"),
                        VocabularyItem("stifle", "sufocar / reprimir")
                    )
                ),
                DialogueLine(
                    id = "ad-4",
                    speaker = "You (Student)",
                    text = "Which is why tiered regulatory thresholds calibrated to compute capacity offer the most pragmatic compromise.",
                    translation = "Razão pela qual limiares regulatórios escalonados e calibrados à capacidade computacional oferecem o compromisso mais pragmático.",
                    pronunciationGuide = "uitch iz uái tí-erd rég-iu-la-to-ri zréch-houlds ké-li-brei-ted tu kom-piút ka-pé-si-ti ó-fer da móust preg-mé-tik kóm-pro-maiz.",
                    keyVocabulary = listOf(
                        VocabularyItem("tiered thresholds", "limiares escalonados"),
                        VocabularyItem("pragmatic compromise", "meio-termo/compromisso pragmático")
                    )
                )
            )
        )
    )
}
