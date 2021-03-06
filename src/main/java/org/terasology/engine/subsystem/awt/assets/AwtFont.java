/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.engine.subsystem.awt.assets;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.assets.AssetType;
import org.terasology.assets.ResourceUrn;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.assets.font.FontCharacter;
import org.terasology.rendering.assets.font.FontData;

public class AwtFont extends org.terasology.rendering.assets.font.Font {
    private static final Logger logger = LoggerFactory.getLogger(AwtFont.class);

    protected FontData data;

    public AwtFont(ResourceUrn urn, AssetType<?, FontData> assetType, FontData data) {
        super(urn, assetType);
        reload(data);
    }

    @Override
    protected void doReload(FontData fontData) {
        this.data = fontData;
    }

    @Override
    public boolean hasCharacter(Character c) {
        return c == '\n' || data.getCharacter(c) != null;
    }

    @Override
    public FontCharacter getCharacterData(Character c) {
        return data.getCharacter(c);
    }

    @Override
    public Vector2i getSize(List<String> lines) {
        BufferedImage graphicsProvider = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = graphicsProvider.createGraphics();
        g2.setFont(getAwtFont());
        FontMetrics fontMetrics = g2.getFontMetrics();

        int height = 0;
        int width = 0;
        for (String line : lines) {
            Rectangle2D stringBounds = fontMetrics.getStringBounds(line, g2);
            width = Math.max(width, (int) stringBounds.getWidth());
            height += stringBounds.getHeight();
        }
        g2.dispose();
        return new Vector2i(width, height);
    }

    @Override
    public int getWidth(String text) {
        BufferedImage graphicsProvider = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = graphicsProvider.createGraphics();
        g2.setFont(getAwtFont());
        FontMetrics fontMetrics = g2.getFontMetrics();

        int largestWidth = 0;
        String[] pieces = text.split("\n");
        for (String string : pieces) {
            Rectangle2D stringBounds = fontMetrics.getStringBounds(string, g2);
            int currentWidth = (int) stringBounds.getWidth();
            largestWidth = Math.max(largestWidth, currentWidth);
        }
        g2.dispose();
        return largestWidth;
    }

    @Override
    public int getWidth(Character c) {
        BufferedImage graphicsProvider = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = graphicsProvider.createGraphics();
        g2.setFont(getAwtFont());
        FontMetrics fontMetrics = g2.getFontMetrics();

        int width = 0;
        if (c != null) {
            Rectangle2D stringBounds = fontMetrics.getStringBounds(Character.valueOf(c).toString(), g2);
            width = (int) stringBounds.getWidth();
        }
        g2.dispose();
        return width;
    }

    @Override
    public int getHeight(String text) {
        BufferedImage graphicsProvider = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = graphicsProvider.createGraphics();
        g2.setFont(getAwtFont());
        FontMetrics fontMetrics = g2.getFontMetrics();

        int height = fontMetrics.getHeight();
        for (char c : text.toCharArray()) {
            if (c == '\n') {
                height += fontMetrics.getHeight();
            }
        }
        g2.dispose();
        return height;
    }

    @Override
    public int getLineHeight() {
        BufferedImage graphicsProvider = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = graphicsProvider.createGraphics();
        g2.setFont(getAwtFont());
        FontMetrics fontMetrics = g2.getFontMetrics();

        int height = fontMetrics.getHeight();
        g2.dispose();

        return height;
    }

    public Font getAwtFont() {
        if (getUrn().toString().equals("engine:default")) {
            return new java.awt.Font("DialogInput", java.awt.Font.BOLD, 14);
        } else if (getUrn().toString().equals("engine:title")) {
            return new java.awt.Font("DialogInput", java.awt.Font.BOLD, 20);
        } else if (getUrn().toString().equals("engine:NotoSans-Regular")) {
            return new java.awt.Font("DialogInput", java.awt.Font.PLAIN, 20);
        } else if (getUrn().toString().equals("engine:NotoSans-Regular-Medium")) {
            return new java.awt.Font("DialogInput", java.awt.Font.PLAIN, 36);
        } else if (getUrn().toString().equals("engine:NotoSans-Regular-Large")) {
            return new java.awt.Font("DialogInput", java.awt.Font.PLAIN, 52);
        } else if (getUrn().toString().equals("engine:NotoSans-Bold")) {
            return new java.awt.Font("DialogInput", java.awt.Font.BOLD, 20);
        } else {
            logger.warn("font '" + getUrn().toString() + "' was not defined.");
        }

        return new java.awt.Font("DialogInput", java.awt.Font.BOLD, 14);
    }

	@Override
	public int getBaseHeight() {
        return data.getBaseHeight();
	}

	@Override
	public int getUnderlineOffset() {
        return data.getUnderlineOffset();
	}

	@Override
	public int getUnderlineThickness() {
        return data.getUnderlineThickness();
	}
}
