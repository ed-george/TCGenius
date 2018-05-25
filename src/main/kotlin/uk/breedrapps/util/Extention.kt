/*
 * (C) Copyright 2018 Breedr Apps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.breedrapps.util

import io.pokemontcg.model.Attack
import io.pokemontcg.model.CardSet
import io.pokemontcg.model.Type
import java.awt.Color

val Boolean.emoji: String
    get() = if(this) "\u2705" else "\u274c"

val CardSet.basicDescription : String
    get() {
        return "\"$name\" ($code)"
    }

val CardSet.fullDescription : String
    get() {
        return """
        |==================================
        | Set: "$name"
        |
        | Series: "$series"
        |
        | Search Code: "$code"
        |
        | PTCGO Code: "$ptcgoCode"
        |
        | Release Date: $releaseDate
        |
        | Card Count: $totalCards
        |
        | Standard Legal: ${standardLegal.emoji}
        |
        | Expanded Legal: ${expandedLegal.emoji}
        |==================================
        """.trimMargin("|")
    }

val Attack.formattedAttack : String
    get() {
        val costEmotes = cost?.map { it.emote }?.joinToString(" ")
        val builder = StringBuilder("$costEmotes \"$name\" [$damage]")

        if(!text.isNullOrEmpty()) builder.append("\n\n$text")

        return builder.toString()
    }

val Type.emote : String?
    get() = when (this) {
        Type.UNKNOWN -> "\u26aa"
        Type.COLORLESS -> "<:Colorless:448929122081832960>"
        Type.DARKNESS -> "<:Darkness:448929122169651220>"
        Type.DRAGON -> "<:Dragon:448929122174107658>"
        Type.FAIRY -> "<:Fairy:448929122274508830>"
        Type.FIGHTING -> "<:Fighting:448929122136227851>"
        Type.FIRE -> "<:Fire:448929122354331658>"
        Type.GRASS -> "<:Grass:448929122312519680>"
        Type.LIGHTNING -> "<:Lightning:448929122362851339>"
        Type.PSYCHIC -> "<:Psychic:448929122417246218>"
        Type.METAL -> "<:Metal:448929122400468992>"
        Type.WATER -> "<:Water:448929122018918411>"
        else -> null
    }

val Type.color : Color
    get() {
        val colorInt = when (this) {
            Type.COLORLESS -> 0xF5F5DA
            Type.DARKNESS -> 0x027798
            Type.DRAGON -> 0xD1A300
            Type.FAIRY -> 0xDD4787
            Type.FIGHTING -> 0xC24635
            Type.FIRE -> 0xD7080C
            Type.GRASS -> 0x427B18
            Type.LIGHTNING -> 0xF9D029
            Type.PSYCHIC -> 0xB139B6
            Type.METAL -> 0xAFAFAF
            Type.WATER -> 0x02B2E6
            else -> 0xCECECE
        }
        return Color(colorInt)
    }