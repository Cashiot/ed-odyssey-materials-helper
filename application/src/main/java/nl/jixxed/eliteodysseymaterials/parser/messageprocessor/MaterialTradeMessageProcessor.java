package nl.jixxed.eliteodysseymaterials.parser.messageprocessor;

import nl.jixxed.eliteodysseymaterials.enums.StoragePool;
import nl.jixxed.eliteodysseymaterials.parser.EncodedTradeParser;
import nl.jixxed.eliteodysseymaterials.parser.ManufacturedTradeParser;
import nl.jixxed.eliteodysseymaterials.parser.RawTradeParser;
import nl.jixxed.eliteodysseymaterials.schemas.journal.MaterialTrade.MaterialTrade;
import nl.jixxed.eliteodysseymaterials.service.event.EventService;
import nl.jixxed.eliteodysseymaterials.service.event.StorageEvent;

public class MaterialTradeMessageProcessor implements MessageProcessor<MaterialTrade> {
    private static final RawTradeParser RAW_TRADE_PARSER = new RawTradeParser();
    private static final EncodedTradeParser ENCODED_TRADE_PARSER = new EncodedTradeParser();
    private static final ManufacturedTradeParser MANUFACTURED_TRADE_PARSER = new ManufacturedTradeParser();

    @Override
    public void process(final MaterialTrade event) {
        final String category = event.getTraderType();
        switch (category) {
            case "raw" ->
                    RAW_TRADE_PARSER.parse(event);
            case "encoded" ->
                    ENCODED_TRADE_PARSER.parse(event);
            case "manufactured" ->
                    MANUFACTURED_TRADE_PARSER.parse(event);
        }
        EventService.publish(new StorageEvent(StoragePool.SHIP));
    }

    @Override
    public Class<MaterialTrade> getMessageClass() {
        return MaterialTrade.class;
    }

}