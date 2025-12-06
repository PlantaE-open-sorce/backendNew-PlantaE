package com.ecotech.plantae.plant.application.internal.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides a static catalog of plant species grouped by category.
 * Acts as a single source of truth for validation and UI consumption.
 */
public class SpeciesCatalogService {

    private final List<Category> catalog;
    private final Set<String> allowedSpecies;

    public SpeciesCatalogService() {
        this.catalog = buildCatalog();
        this.allowedSpecies = catalog.stream()
                .flatMap(cat -> cat.groups().stream())
                .flatMap(group -> group.items().stream())
                .map(item -> item.value().toLowerCase())
                .collect(Collectors.toSet());
    }

    public List<Category> getCatalog() {
        return Collections.unmodifiableList(catalog);
    }

    public boolean isValidSpecies(String species) {
        if (species == null || species.isBlank()) return false;
        return allowedSpecies.contains(species.trim().toLowerCase());
    }

    private List<Category> buildCatalog() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("tropical", "🌿 Tropical / Interior", List.of(
                group("Monstera / Philodendron", map(
                        "Monstera deliciosa", "Monstera deliciosa",
                        "Monstera adansonii", "Monstera adansonii",
                        "Philodendron hederaceum (Hoja de corazón)", "Philodendron hederaceum",
                        "Philodendron 'Birkin'", "Philodendron 'Birkin'",
                        "Philodendron erubescens (Pink Princess)", "Philodendron erubescens")),
                group("Pothos / Syngonium", map(
                        "Epipremnum aureum (Potos dorado)", "Epipremnum aureum",
                        "Epipremnum aureum 'Neon'", "Epipremnum aureum 'Neon'",
                        "Epipremnum aureum 'Marble Queen'", "Epipremnum aureum 'Marble Queen'",
                        "Syngonium podophyllum (Singonio)", "Syngonium podophyllum")),
                group("Calatheas / Marantas", map(
                        "Stromanthe sanguinea (Calathea Triostar)", "Stromanthe sanguinea",
                        "Goeppertia orbifolia (Calathea Orbifolia)", "Goeppertia orbifolia",
                        "Goeppertia veitchiana (Calathea Medallion)", "Goeppertia veitchiana",
                        "Maranta leuconeura (Maranta tricolor)", "Maranta leuconeura")),
                group("Alocasias / Aroides", map(
                        "Alocasia amazonica (Alocasia Polly)", "Alocasia amazonica",
                        "Alocasia zebrina", "Alocasia zebrina",
                        "Alocasia macrorrhizos (Oreja de elefante)", "Alocasia macrorrhizos")),
                group("Palmas / Ficus", map(
                        "Dypsis lutescens (Palma Areca)", "Dypsis lutescens",
                        "Howea forsteriana (Palma Kentia)", "Howea forsteriana",
                        "Chamaedorea elegans (Palma de salón)", "Chamaedorea elegans",
                        "Ficus elastica (Gomero)", "Ficus elastica",
                        "Ficus lyrata", "Ficus lyrata",
                        "Ficus benjamina", "Ficus benjamina")),
                group("Otros tropicales", map(
                        "Spathiphyllum wallisii (Cuna de Moisés)", "Spathiphyllum wallisii",
                        "Anthurium andraeanum (Anturio rojo)", "Anthurium andraeanum",
                        "Asplenium nidus (Helecho nido de ave)", "Asplenium nidus",
                        "Platycerium bifurcatum (Helecho cuerno de alce)", "Platycerium bifurcatum",
                        "Begonia maculata", "Begonia maculata",
                        "Begonia rex", "Begonia rex",
                        "Aglaonema Silver Bay", "Aglaonema commutatum"))
        )));

        list.add(new Category("succulents", "🌵 Árido / Suculentas", List.of(
                group("Aloe / Sansevieria", map(
                        "Aloe vera", "Aloe vera",
                        "Aloe arborescens", "Aloe arborescens",
                        "Dracaena trifasciata (Sansevieria)", "Dracaena trifasciata",
                        "Dracaena angolensis (Sansevieria cylindrica)", "Dracaena angolensis",
                        "Dracaena trifasciata 'Moonshine'", "Dracaena trifasciata 'Moonshine'")),
                group("Crassulas / Echeverias", map(
                        "Crassula ovata (Árbol de jade)", "Crassula ovata",
                        "Crassula marnieriana (Collar de jade)", "Crassula marnieriana",
                        "Echeveria elegans", "Echeveria elegans",
                        "Echeveria affinis (Black Prince)", "Echeveria affinis",
                        "Echeveria 'Lola'", "Echeveria 'Lola'")),
                group("Haworthia / Sedum", map(
                        "Haworthiopsis fasciata (Cebra)", "Haworthiopsis fasciata",
                        "Haworthia cooperi", "Haworthia cooperi",
                        "Sedum morganianum (Cola de burro)", "Sedum morganianum",
                        "Sedum rubrotinctum", "Sedum rubrotinctum")),
                group("Cactus y mixtas", map(
                        "Schlumbergera truncata (Cactus de Navidad)", "Schlumbergera truncata",
                        "Epiphyllum anguliger (Cactus espina de pescado)", "Epiphyllum anguliger",
                        "Opuntia microdasys", "Opuntia microdasys",
                        "Euphorbia trigona", "Euphorbia trigona",
                        "Senecio rowleyanus (Rosario)", "Senecio rowleyanus",
                        "Echinopsis pachanoi (San Pedro)", "Echinopsis pachanoi",
                        "Lithops spp.", "Lithops spp.",
                        "Beaucarnea recurvata (Pata de elefante)", "Beaucarnea recurvata",
                        "Yucca elephantipes", "Yucca elephantipes"))
        )));

        list.add(new Category("edible", "🍅 Huerto urbano", List.of(
                group("Hortalizas y frutos", map(
                        "Tomate cherry", "Solanum lycopersicum var. cerasiforme",
                        "Tomate Raf", "Solanum lycopersicum",
                        "Pimiento morrón", "Capsicum annuum",
                        "Ají / Chile", "Capsicum frutescens",
                        "Pepino", "Cucumis sativus",
                        "Calabacín / Zucchini", "Cucurbita pepo",
                        "Berenjena", "Solanum melongena",
                        "Fresa / Frutilla", "Fragaria × ananassa")),
                group("Hierbas aromáticas", map(
                        "Albahaca genovesa", "Ocimum basilicum",
                        "Menta piperita", "Mentha × piperita",
                        "Hierbabuena", "Mentha spicata",
                        "Romero", "Salvia rosmarinus",
                        "Tomillo", "Thymus vulgaris",
                        "Orégano", "Origanum vulgare",
                        "Cilantro", "Coriandrum sativum",
                        "Perejil", "Petroselinum crispum",
                        "Cebollino", "Allium schoenoprasum",
                        "Eneldo", "Anethum graveolens")),
                group("Hoja verde", map(
                        "Lechuga romana", "Lactuca sativa var. longifolia",
                        "Espinaca", "Spinacia oleracea",
                        "Acelga", "Beta vulgaris subsp. vulgaris",
                        "Rúcula", "Eruca vesicaria")),
                group("Cítricos en maceta", map(
                        "Limonero en maceta", "Citrus × limon",
                        "Kumquat / Naranjo chino", "Citrus japonica"))
        )));

        list.add(new Category("flower", "🌸 Florales", List.of(
                group("Orquídeas y delicadas", map(
                        "Phalaenopsis (Orquídea)", "Phalaenopsis spp.",
                        "Dendrobium (Orquídea)", "Dendrobium spp.",
                        "Vanda (Orquídea)", "Vanda spp.",
                        "Violeta africana", "Saintpaulia ionantha",
                        "Gardenia", "Gardenia jasminoides")),
                group("Ácido / sombra parcial", map(
                        "Azalea", "Rhododendron simsii",
                        "Hortensia", "Hydrangea macrophylla",
                        "Ciclamen", "Cyclamen persicum")),
                group("Florales de interior", map(
                        "Rosa miniatura", "Rosa chinensis 'Minima'",
                        "Geranio zonal", "Pelargonium × hortorum",
                        "Begonia Elatior", "Begonia × hiemalis",
                        "Amarilis", "Hippeastrum spp.",
                        "Clivia", "Clivia miniata",
                        "Jazmín de Madagascar", "Stephanotis floribunda",
                        "Flor de Pascua / Poinsettia", "Euphorbia pulcherrima"))
        )));

        list.add(new Category("resilient", "🌳 Resistentes", List.of(
                group("Muy resistentes", map(
                        "Zamioculcas zamiifolia (ZZ plant)", "Zamioculcas zamiifolia",
                        "Aspidistra", "Aspidistra elatior",
                        "Chlorophytum comosum (Cinta)", "Chlorophytum comosum",
                        "Hedera helix (Hiedra)", "Hedera helix")),
                group("Peperomias y drácenas", map(
                        "Peperomia obtusifolia", "Peperomia obtusifolia",
                        "Peperomia caperata", "Peperomia caperata",
                        "Dracaena sanderiana (Bambú de la suerte)", "Dracaena sanderiana",
                        "Dracaena marginata", "Dracaena marginata",
                        "Dracaena fragrans 'Compacta'", "Dracaena fragrans 'Compacta'",
                        "Dracaena fragrans (Palo de Brasil)", "Dracaena fragrans")),
                group("Trepadoras y otras", map(
                        "Schefflera (Árbol paraguas)", "Schefflera arboricola",
                        "Tradescantia zebrina", "Tradescantia zebrina",
                        "Tradescantia albiflora 'Nanouk'", "Tradescantia albiflora 'Nanouk'",
                        "Pilea peperomioides", "Pilea peperomioides",
                        "Plectranthus verticillatus (Planta del dólar)", "Plectranthus verticillatus"))
        )));

        return list;
    }

    private Group group(String name, Map<String, String> items) {
        List<Option> options = items.entrySet().stream()
                .map(e -> new Option(e.getKey(), e.getValue()))
                .toList();
        return new Group(name, options);
    }

    private Map<String, String> map(String... values) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < values.length - 1; i += 2) {
            map.put(values[i], values[i + 1]);
        }
        return map;
    }

    public record Category(String key, String label, List<Group> groups) {
    }

    public record Group(String name, List<Option> items) {
    }

    public record Option(String label, String value) {
    }
}
