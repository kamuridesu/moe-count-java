package com.kamuridesu.count.domain.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class SVGMergerService {

    private static final String SVG_HEADER_TEMPLATE = """
                <svg width="270" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                %s
                </svg>
                """;
    private static final String G_HEADER_TEMPLATE = """
                <g xmlns="http://www.w3.org/2000/svg" id="id%d:id%d" transform="translate(%d.0,0.0)">
                %s
                </g>
                """;
    private static final int X_COORD_INCREMENT = 45;

    private Map<String, String> cachedSvg = new HashMap<>();

    private String loadSVG(String filename) throws IOException {
        if (this.cachedSvg.containsKey(filename)) {
            return this.cachedSvg.get(filename);
        }
        var resource = new ClassPathResource("static/" + filename);
        var path = resource.getFile().toPath();
        var data = Files.readString(path);
        this.cachedSvg.put(filename, data);
        return data;
    }

    public String merge(int number) throws IOException {
        StringBuilder mergedSVG = new StringBuilder();
        var xCoord = 0;
        var digits = String.format("%06d", number).toCharArray();

        for (int i = 0; i < digits.length; i++) {
            var file = loadSVG(digits[i] + ".svg");
            var gElem = String.format(G_HEADER_TEMPLATE, i, i, xCoord, file);
            mergedSVG.append(gElem);
            xCoord += X_COORD_INCREMENT;
        }

        return String.format(SVG_HEADER_TEMPLATE, mergedSVG.toString());
    }
}
