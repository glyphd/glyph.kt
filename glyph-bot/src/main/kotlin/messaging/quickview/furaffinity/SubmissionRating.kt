/*
 * SubmissionRating.kt
 *
 * Glyph, a Discord bot that uses natural language instead of commands
 * powered by DialogFlow and Kotlin
 *
 * Copyright (C) 2017-2021 by Ian Moore
 *
 * This file is part of Glyph.
 *
 * Glyph is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.yttr.glyph.bot.messaging.quickview.furaffinity

import java.awt.Color

/**
 * The submission rating (maturity level) of a FurAffinity submission
 */
enum class SubmissionRating(
    /**
     * The color of the rating (based on the colors used on furaffinity.net)
     */
    val color: Color,
    /**
     * Whether or not the rating is considered a NSFW rating
     */
    val nsfw: Boolean
) {
    /**
     * Suitable for all-ages
     */
    General(Color.decode("#79A977"), false),

    /**
     * Gore, violence or tasteful/artistic nudity or mature themes.
     */
    Mature(Color.decode("#697CC1"), true),

    /**
     * Explicit or imagery otherwise geared towards adult audiences.
     */
    Adult(Color.decode("#992D22"), true)
}
