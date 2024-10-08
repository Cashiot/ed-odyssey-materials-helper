package nl.jixxed.eliteodysseymaterials.parser;

import lombok.extern.slf4j.Slf4j;
import nl.jixxed.eliteodysseymaterials.enums.Encoded;
import nl.jixxed.eliteodysseymaterials.schemas.journal.MaterialTrade.MaterialTrade;
import nl.jixxed.eliteodysseymaterials.schemas.journal.MaterialTrade.Paid;
import nl.jixxed.eliteodysseymaterials.schemas.journal.MaterialTrade.Received;
import nl.jixxed.eliteodysseymaterials.service.StorageService;

@Slf4j
public class EncodedTradeParser implements HorizonsParser<MaterialTrade> {

    @Override
    public void parse(final MaterialTrade event) {
        final Paid paid = event.getPaid();
        final String paidName = paid.getMaterial();
        final int paidAmount = paid.getQuantity().intValue();
        final Received received = event.getReceived();
        final String receivedName = received.getMaterial();
        final int receivedAmount = received.getQuantity().intValue();
        final Encoded paidMaterial = Encoded.forName(paidName);
        if (Encoded.UNKNOWN.equals(paidMaterial)) {
            log.warn("Unknown Paid Encoded data detected: " + event);
        } else {
            StorageService.removeMaterial(paidMaterial, paidAmount);
        }
        final Encoded receivedMaterial = Encoded.forName(receivedName);
        if (Encoded.UNKNOWN.equals(receivedMaterial)) {
            log.warn("Unknown Received Encoded data detected: " + event);
        } else {
            StorageService.addMaterial(receivedMaterial, receivedAmount);
        }
    }
}
