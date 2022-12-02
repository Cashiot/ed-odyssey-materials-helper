package nl.jixxed.eliteodysseymaterials.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.jixxed.eliteodysseymaterials.enums.JournalEventType;
import nl.jixxed.eliteodysseymaterials.parser.messageprocessor.*;
import nl.jixxed.eliteodysseymaterials.service.event.EventService;
import nl.jixxed.eliteodysseymaterials.service.event.JournalLineProcessedEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MessageHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<JournalEventType, MessageProcessor> messageProcessors = Map.ofEntries(
            Map.entry(JournalEventType.COMMANDER, new CommanderMessageProcessor()),
            Map.entry(JournalEventType.ENGINEERPROGRESS, new EngineerProgressMessageProcessor()),
            Map.entry(JournalEventType.EMBARK, new EmbarkMessageProcessor()),
            Map.entry(JournalEventType.SHIPLOCKER, new ShipLockerMessageProcessor()),
            Map.entry(JournalEventType.BACKPACK, new BackpackMessageProcessor()),
            Map.entry(JournalEventType.FLEETCARRIER, new FleetCarrierMessageProcessor()),
            Map.entry(JournalEventType.BACKPACKCHANGE, new BackpackChangeMessageProcessor()),
            Map.entry(JournalEventType.RESUPPLY, new ResupplyMessageProcessor()),
            Map.entry(JournalEventType.FSDJUMP, new FSDJumpMessageProcessor()),
            Map.entry(JournalEventType.LOCATION, new LocationMessageProcessor()),
            Map.entry(JournalEventType.TOUCHDOWN, new TouchdownMessageProcessor()),
            Map.entry(JournalEventType.UNDOCKED, new UndockedMessageProcessor()),
            Map.entry(JournalEventType.LIFTOFF, new LiftOffMessageProcessor()),
            Map.entry(JournalEventType.APPROACHBODY, new ApproachBodyMessageProcessor()),
            Map.entry(JournalEventType.APPROACHSETTLEMENT, new ApproachSettlementMessageProcessor()),
            Map.entry(JournalEventType.SUPERCRUISEENTRY, new SupercruiseEntryMessageProcessor()),
            Map.entry(JournalEventType.LEAVEBODY, new LeaveBodyMessageProcessor()),
            Map.entry(JournalEventType.DOCKED, new DockedMessageProcessor()),
            Map.entry(JournalEventType.MATERIALS, new MaterialsMessageProcessor()),
            Map.entry(JournalEventType.CARGO, new CargoMessageProcessor()),
            Map.entry(JournalEventType.CARGOTRANSFER, new CargoTransferMessageProcessor()),

            Map.entry(JournalEventType.SUITLOADOUT, new SuitLoadoutMessageProcessor()),
            Map.entry(JournalEventType.SWITCHSUITLOADOUT, new SuitLoadoutMessageProcessor()),

            Map.entry(JournalEventType.MATERIALCOLLECTED, new MaterialCollectedMessageProcessor()),
            Map.entry(JournalEventType.MATERIALTRADE, new MaterialTradeMessageProcessor()),
            Map.entry(JournalEventType.MATERIALDISCARDED, new MaterialDiscardedMessageProcessor()),
            Map.entry(JournalEventType.ENGINEERCONTRIBUTION, new EngineerContributionMessageProcessor()),
            Map.entry(JournalEventType.ENGINEERCRAFT, new EngineerCraftMessageProcessor()),
            Map.entry(JournalEventType.MISSIONCOMPLETED, new MissionCompletedMessageProcessor()),
            Map.entry(JournalEventType.SYNTHESIS, new SynthesisMessageProcessor()),
            Map.entry(JournalEventType.TECHNOLOGYBROKER, new TechnologyBrokerMessageProcessor()),
            Map.entry(JournalEventType.UPGRADESUIT, new UpgradeSuitMessageProcessor()),
            Map.entry(JournalEventType.UPGRADEWEAPON, new UpgradeWeaponMessageProcessor()),
            Map.entry(JournalEventType.RECEIVETEXT, new ReceiveTextMessageProcessor()),
            Map.entry(JournalEventType.SENDTEXT, new SendTextMessageProcessor()),
            Map.entry(JournalEventType.BUYMICRORESOURCES, new BuyMicroResourcesMessageProcessor()),
            Map.entry(JournalEventType.MARKETBUY, new MarketBuyMessageProcessor()),
            Map.entry(JournalEventType.MARKETSELL, new MarketSellMessageProcessor()),

            Map.entry(JournalEventType.LOADGAME, new LoadGameMessageProcessor())
    );
    private static final String EVENT = "event";

    static void handleMessage(final String message, final File file) {
        try {
            final JsonNode jsonNode = OBJECT_MAPPER.readTree(message);
            if (jsonNode.get(EVENT) != null) {
                log.info("event: " + jsonNode.get(EVENT).asText());
                final JournalEventType journalEventType = JournalEventType.forName(jsonNode.get(EVENT).asText());
                final MessageProcessor messageProcessor = messageProcessors.get(journalEventType);
                if (messageProcessor != null) {
                    messageProcessor.process(jsonNode);
                    EventService.publish(new JournalLineProcessedEvent(jsonNode.get("timestamp").asText(), journalEventType, file));
                }
            } else {
                log.warn("EVENT NULL: " + jsonNode.toPrettyString());
            }
        } catch (final JsonProcessingException e) {
            log.error("Error processing json message", e);
        }
    }

    static void handleMessage(final File file, final JournalEventType journalEventType) {
        try {
            final String message = Files.readString(file.toPath());
            final JsonNode jsonNode = OBJECT_MAPPER.readTree(message);
            log.info("event: " + journalEventType);
            final MessageProcessor messageProcessor = messageProcessors.get(journalEventType);
            if (messageProcessor != null) {
                messageProcessor.process(jsonNode);
                EventService.publish(new JournalLineProcessedEvent("now", journalEventType, file));
            }
        } catch (final JsonProcessingException e) {
            log.error("Error processing json message", e);
        } catch (final IOException e) {
            log.error("Error processing ShipLocker or Backpack", e);
        }
    }
}
