package com.example.dialogoo.data

import com.example.dialogoo.model.ArenaChallenge
import com.example.dialogoo.model.Level

object ArenaChallenges {
    val pool: List<ArenaChallenge> = listOf(
        ArenaChallenge(
            id = "ch-1",
            title = "Hotel Check-in & Breakfast",
            situation = "Perguntando ao recepcionista sobre horários e localização do café.",
            targetLine = "Excuse me, could you tell me what time breakfast is served and where the dining room is located?",
            translation = "Com licença, você poderia me dizer a que horas o café da manhã é servido e onde fica o refeitório?",
            level = Level.A2,
            coverDrawableRes = "cover_coffee_shop"
        ),
        ArenaChallenge(
            id = "ch-2",
            title = "Museum Ticket Desk",
            situation = "Comprando ingressos na bilheteria do museu de arte moderna.",
            targetLine = "Hi, I'd like to buy two adult tickets for today's exhibition, please.",
            translation = "Oi, eu gostaria de comprar dois ingressos de adulto para a exposição de hoje, por favor.",
            level = Level.A1,
            coverDrawableRes = "cover_city_directions"
        ),
        ArenaChallenge(
            id = "ch-3",
            title = "Weather Small Talk",
            situation = "Conversando casualmente sobre a previsão do tempo com seu vizinho.",
            targetLine = "It looks like it's going to rain this afternoon. I should probably bring an umbrella just in case.",
            translation = "Parece que vai chover esta tarde. Provavelmente devo levar um guarda-chuva por precaução.",
            level = Level.A2,
            coverDrawableRes = "cover_first_day_work"
        ),
        ArenaChallenge(
            id = "ch-4",
            title = "Tech Support Call",
            situation = "Reportando um bug recorrente no notebook ao suporte de TI.",
            targetLine = "I have been having trouble with my laptop. The screen keeps freezing and I cannot seem to fix it on my own.",
            translation = "Estou tendo problemas com meu notebook. A tela continua travando e não consigo consertar sozinho.",
            level = Level.B1,
            coverDrawableRes = "cover_job_interview"
        ),
        ArenaChallenge(
            id = "ch-5",
            title = "Team Results Presentation",
            situation = "Apresentando métricas de satisfação do usuário na reunião de diretoria.",
            targetLine = "Based on our analysis, customer satisfaction has increased by fifteen percent compared to last quarter.",
            translation = "Com base em nossa análise, a satisfação do cliente aumentou quinze por cento em comparação ao último trimestre.",
            level = Level.B2,
            coverDrawableRes = "cover_business_negotiation"
        ),
        ArenaChallenge(
            id = "ch-6",
            title = "Uber & Ride Confirmation",
            situation = "Confirmando a corrida e o endereço de destino com o motorista.",
            targetLine = "Hi, are you my Uber driver? I am going to the downtown convention center on Fifth Avenue.",
            translation = "Oi, você é meu motorista da Uber? Vou para o centro de convenções no centro na Quinta Avenida.",
            level = Level.A1,
            coverDrawableRes = "cover_travel_conversation"
        ),
        ArenaChallenge(
            id = "ch-7",
            title = "Salary & Benefits Discussion",
            situation = "Propondo ajustes no pacote de remuneração em rodada final com o recrutador.",
            targetLine = "I appreciate the offer, but given my experience and the market rate, I was hoping we could discuss a slightly higher compensation package.",
            translation = "Agradeço a oferta, mas dada a minha experiência e o valor de mercado, esperava que pudéssemos discutir um pacote de remuneração um pouco superior.",
            level = Level.C1,
            coverDrawableRes = "cover_academic_debate"
        ),
        ArenaChallenge(
            id = "ch-8",
            title = "Airport International Check-in",
            situation = "Apresentando passaporte e despachando malas para Los Angeles.",
            targetLine = "Good morning! I would like to check in for the ten thirty flight to Los Angeles. Here is my passport and booking confirmation.",
            translation = "Bom dia! Gostaria de fazer o check-in para o voo das dez e meia para Los Angeles. Aqui está meu passaporte e confirmação de reserva.",
            level = Level.A2,
            coverDrawableRes = "cover_restaurant_order"
        )
    )
}
