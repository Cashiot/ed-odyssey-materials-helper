package nl.jixxed.eliteodysseymaterials.templates.horizons.shipbuilder.stats;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import nl.jixxed.eliteodysseymaterials.builder.LabelBuilder;
import nl.jixxed.eliteodysseymaterials.domain.ApplicationState;
import nl.jixxed.eliteodysseymaterials.domain.ships.Ship;
import nl.jixxed.eliteodysseymaterials.helper.Formatters;
import nl.jixxed.eliteodysseymaterials.templates.components.segmentbar.SegmentType;
import nl.jixxed.eliteodysseymaterials.templates.components.segmentbar.TypeSegment;
import nl.jixxed.eliteodysseymaterials.templates.components.segmentbar.TypeSegmentView;
import org.controlsfx.control.SegmentedBar;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static nl.jixxed.eliteodysseymaterials.templates.horizons.shipbuilder.stats.PowerStats.*;

public class PowerBar extends VBox {

    private SegmentedBar<TypeSegment> segmentedBar;
    private TypeSegment groupP;
    private TypeSegment group1;
    private TypeSegment group2;
    private TypeSegment group3;
    private TypeSegment group4;
    private TypeSegment group5;
    private TypeSegment groupAvailable;
    private TypeSegment groupOverPower;
    private double overPower = 0D;
    private boolean retracted;
    private DoubleProperty overPowerFactor = new SimpleDoubleProperty(1D);
    private DoubleProperty availableFactor = new SimpleDoubleProperty(1D);
    public PowerBar(boolean retracted) {
        this.retracted = retracted;
        this.segmentedBar = new SegmentedBar<>();
        this.segmentedBar.getStyleClass().add("power-progressbar");
        this.segmentedBar.setOrientation(Orientation.HORIZONTAL);
        this.segmentedBar.setInfoNodeFactory(segment -> {
            final Map<Integer, Double> power = retracted ? calculateRetractedPower() : calculateDeployedPower();
            Double temp = 0D;
            Double percentage = switch (segment.getSegmentType()) {
                case POWER_GROUP_5:
                    temp += power.get(5);
                case POWER_GROUP_4:
                    temp += power.get(4);
                case POWER_GROUP_3:
                    temp += power.get(3);
                case POWER_GROUP_2:
                    temp += power.get(2);
                case POWER_GROUP_1:
                    temp += power.get(1);
                case POWER_GROUP_P:
                    yield temp + power.get(-1);
                case POWER_GROUP_NONE:
                    double usedPower1 = power.get(-1) + power.get(1) + power.get(2) + power.get(3) + power.get(4) + power.get(5);
                    final double available1 = power.get(0) - usedPower1;
                    yield Math.max(available1, 0D);
                case POWER_OVERPOWER:
                    double usedPower2 = power.get(-1) + power.get(1) + power.get(2) + power.get(3) + power.get(4) + power.get(5);
                    final double available2 = power.get(0) - usedPower2;
                    overPower = Math.abs(Math.min(available2, 0D));
                    yield overPower;
                default:
                    yield 0.0;
            } / power.get(0) * 100;
            return LabelBuilder.builder()
                    .withStyleClass("power-progressbar-label")
                    .withNonLocalizedText(segment.getText() + ": " + Formatters.NUMBER_FORMAT_2.format(segment.getSegmentType() == SegmentType.POWER_OVERPOWER ? overPower : segment.getValue()) + "MW (" + Formatters.NUMBER_FORMAT_1_CEIL.format(percentage) + "%)")
                    .build();
        });
        this.segmentedBar.setSegmentViewFactory(segment -> new TypeSegmentView(segment, Map.of(
                SegmentType.POWER_GROUP_P, POWER_GROUP_P_COLOR,
                SegmentType.POWER_GROUP_1, POWER_GROUP_1_COLOR,
                SegmentType.POWER_GROUP_2, POWER_GROUP_2_COLOR,
                SegmentType.POWER_GROUP_3, POWER_GROUP_3_COLOR,
                SegmentType.POWER_GROUP_4, POWER_GROUP_4_COLOR,
                SegmentType.POWER_GROUP_5, POWER_GROUP_5_COLOR,
                SegmentType.POWER_GROUP_NONE, Color.rgb(128, 128, 128),
                SegmentType.POWER_OVERPOWER, Color.rgb(240, 20, 20)
        ), false));
        final Map<Integer, Double> power = calculateRetractedPower();
        this.groupP = new TypeSegment(power.get(-1), SegmentType.POWER_GROUP_P);
        this.group1 = new TypeSegment(power.get(1), SegmentType.POWER_GROUP_1);
        this.group2 = new TypeSegment(power.get(2), SegmentType.POWER_GROUP_2);
        this.group3 = new TypeSegment(power.get(3), SegmentType.POWER_GROUP_3);
        this.group4 = new TypeSegment(power.get(4), SegmentType.POWER_GROUP_4);
        this.group5 = new TypeSegment(power.get(5), SegmentType.POWER_GROUP_5);
        this.groupOverPower = new TypeSegment(0D, SegmentType.POWER_OVERPOWER);
        this.groupAvailable = new TypeSegment(power.get(0) - power.get(-1) - power.get(1) - power.get(2) - power.get(3) - power.get(4) - power.get(5), SegmentType.POWER_GROUP_NONE);
        this.segmentedBar.getSegments().addAll(groupP, group1, group2, group3, group4, group5, groupAvailable, groupOverPower);
        final Line lineDestroyedAndMalfunction = createLine(0.2);
        final Line lineMalfunction = createLine(0.4);
        final Line lineDestroyed = createLine(0.5);
        final Pane stackPane = new Pane(this.segmentedBar, lineDestroyedAndMalfunction, lineMalfunction, lineDestroyed);
        this.getChildren().add(stackPane);
    }

    private Line createLine(double percentage) {
        Line lineMalfunction = new Line();
        lineMalfunction.getStyleClass().add("power-progressbar-line");
//        lineMalfunction.setStartX(0D);
        lineMalfunction.startXProperty().bind(this.segmentedBar.widthProperty().divide(overPowerFactor).divide(availableFactor).multiply(percentage));
        lineMalfunction.endXProperty().bind(this.segmentedBar.widthProperty().divide(overPowerFactor).divide(availableFactor).multiply(percentage));
        lineMalfunction.endYProperty().bind(this.segmentedBar.heightProperty());
        return lineMalfunction;
    }

    public void update() {
        final Map<Integer, Double> power = retracted ? calculateRetractedPower() : calculateDeployedPower();

        groupP.setValue(power.get(-1));
        group1.setValue(power.get(1));
        group2.setValue(power.get(2));
        group3.setValue(power.get(3));
        group4.setValue(power.get(4));
        group5.setValue(power.get(5));
        double usedPower = power.get(-1) + power.get(1) + power.get(2) + power.get(3) + power.get(4) + power.get(5);
        final double available = power.get(0) - usedPower;
        groupAvailable.setValue(Math.max(available, 0D));
//        groupOverPower.setValue(Math.abs(Math.min(available, 0D)));
        overPower = Math.abs(Math.min(available, 0D));
        overPowerFactor.set(overPower > 0D ? 1.1 : 1D);
        availableFactor.set(overPower > 0D ? (power.get(0) + overPower) / power.get(0) : 1D);
        groupOverPower.setValue(overPower > 0D ? usedPower / 10D : 0D);

    }

    private Map<Integer, Double> calculateRetractedPower() {
        return getShip().map(Ship::getRetractedPower).orElseGet(() -> new HashMap<>(Map.of(
                -1, 0D,
                0, 0D,
                1, 0D,
                2, 0D,
                3, 0D,
                4, 0D,
                5, 0D
        )));
    }

    private Map<Integer, Double> calculateDeployedPower() {
        return getShip().map(Ship::getDeployedPower).orElseGet(() -> new HashMap<>(Map.of(
                -1, 0D,
                0, 0D,
                1, 0D,
                2, 0D,
                3, 0D,
                4, 0D,
                5, 0D
        )));
    }

    public Optional<Ship> getShip() {
        return Optional.ofNullable(ApplicationState.getInstance().getShip());
    }

}
