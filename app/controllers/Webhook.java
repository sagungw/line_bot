package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineMessagingClientImpl;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import play.Logger;
import play.api.Configuration;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


public class Webhook extends Controller {

    private String lineChannelToken;

    @Inject
    public Webhook(Configuration configuration) {
        this.lineChannelToken = configuration.underlying().getString("line.channel-token");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result webhook() {
        JsonNode root = request().body().asJson();

        JsonNode events = root.get("events");

        for (final JsonNode event : events) {
            Logger.info(Json.prettyPrint(event));

            String replyToken = event.get("replyToken").asText();

            List<Action> actions = new ArrayList<>();
            actions.add(new PostbackAction("Ini label", "action=buy&itemid=222", "ini text"));
            actions.add(new MessageAction("ini label", "ini text"));

            ButtonsTemplate template = new ButtonsTemplate("http://www.21cineplex.com/data/gallery/pictures/148792288417018_300x430.jpg", "ini title", "ini text", actions);

            TemplateMessage msg = new TemplateMessage("Buttons template", template);

            try {
                LineMessagingClient client = new LineMessagingClientImpl(LineMessagingServiceBuilder.create(this.lineChannelToken).build());
                BotApiResponse response = client.replyMessage(new ReplyMessage(replyToken, msg)).get();

                response.getDetails().forEach(s -> Logger.info("Details: " + s));
                Logger.info("Message: " + response.getMessage());

//                Logger.info("Code: " + Integer.toString(response.code()));
//                Logger.info("Message: " + response.message());
//                Logger.info("Error: " + response.raw().body().toString());
//                Logger.info("Error: " + response.raw().message().toString());

                return ok(response.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ok("nothing");
    }

}
