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

package uk.breedrapps.command

import com.jagrosh.jdautilities.command.CommandEvent
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission
import uk.breedrapps.command.base.BaseCommand
import uk.breedrapps.util.color
import uk.breedrapps.util.emote
import uk.breedrapps.util.formattedAttack
import java.awt.Color
import java.util.regex.Pattern


class SearchCardCommand : BaseCommand() {

    init {
        name = "card"
        help = "Searches TCG database for a given card"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
        cooldown = 30
    }

    override fun searchForTerm(event: CommandEvent) {

        if(event.args.length < 4) {
            event.replyWarning("Please enter a longer search term")
            return
        }

        var cardId: String? = null
        var cardName = event.args

        val matcher = Pattern.compile("[\\(\\{\\[](.*?)[\\]\\}\\)]").matcher(event.args)

        if (matcher.find()) {
            cardId = matcher.group(1)
        }

        cardId?.let {
            cardName = cardName.replace(matcher.group(0), "")
        }

        api.card().where {
            id = cardId
            name = cardName.trim()
        }.observeAll()
                .flatMapIterable { it }
                .doOnError {
                    it.printStackTrace()
                    event.replyError("Something went boom!")
                }
                .take(1)
                .forEach {

                    val builder = EmbedBuilder()
                            .setColor(if(it.types != null) it.types!![0].color else Color.BLACK)
                            .setImage(it.imageUrlHiRes)
                            .addField("Name", it.name, true)
                            .addField("Id", it.id, true)

                    val superType = when(it.supertype) {
                        SuperType.UNKNOWN -> null
                        else -> it.supertype.displayName
                    }

                    val subType = when(it.subtype) {
                        SubType.UNKNOWN -> null
                        else -> it.subtype.displayName
                    }

                    superType?.let {
                        builder.addField("Card Type", "$it - ${subType ?: "(No Subtype)"}", true)
                    }

                    it.types?.let {
                        builder.addField("Type", it.map { it.emote }.joinToString(" "), true)
                    }

                    it.hp?.let {
                        builder.addField("HP", "$it", true)
                    }

                    builder.addField("Set", "${it.series} - ${it.set} (${it.setCode})", true)

                    it.ability?.let {
                        builder.addField(it.type, it.text, false)
                    }

                    it.text?.let {
                        it.forEachIndexed { index, text ->
                            builder.addField(
                                    "Text".appendIndexSuffix(index, it),
                                    text,
                                    false
                            )
                        }
                    }

                    it.attacks?.let {
                        it.forEachIndexed { index, attack ->
                            builder.addField(
                                    "Attack".appendIndexSuffix(index, it),
                                    attack.formattedAttack,
                                    false
                            )
                        }
                    }

                    it.weaknesses?.let {
                        val emote = it.map { it.type.emote }
                        it.forEachIndexed { index, effect ->
                            builder.addField(
                                    "Weakness".appendIndexSuffix(index, it),
                                    "${emote[index]} ${effect.value}",
                                    true
                            )
                        }
                    }

                    it.resistances?.let {
                        val emote = it.map { it.type.emote }
                        it.forEachIndexed { index, effect ->
                            builder.addField(
                                    "Resistance".appendIndexSuffix(index, it),
                                    "${emote[index]} ${effect.value}",
                                    true
                            )
                        }
                    }

                    it.retreatCost?.let {
                        val emotes = it.map { it.emote }.joinToString(" ")
                        builder.addField("Retreat Cost", emotes, true)
                    }

                    event.reply(builder.build())
                }
    }

}

// Helper method for appending a given index to a string
fun String.appendIndexSuffix(index: Int, collection: Collection<*>): String {
    return if (collection.size > 1) "$this ${index + 1}" else this
}