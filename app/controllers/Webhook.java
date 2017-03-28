package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineMessagingClientImpl;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import models.Theater;
import models.TheaterMovie;
import org.apache.commons.lang3.StringUtils;
import play.api.Configuration;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.TheaterMovieRepository;
import repositories.TheaterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Webhook extends Controller {

    private String lineChannelToken;

    private TheaterMovieRepository theaterMovieRepository;

    private TheaterRepository theaterRepository;

    @Inject
    public Webhook(Configuration configuration, TheaterMovieRepository theaterMovieRepository, TheaterRepository theaterRepository) {
        this.lineChannelToken = configuration.underlying().getString("line.channel-token");
        this.theaterMovieRepository = theaterMovieRepository;
        this.theaterRepository = theaterRepository;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result webhook() {
        JsonNode root = request().body().asJson();

        JsonNode events = root.get("events");

        for (final JsonNode event : events) {
            String replyToken = event.get("replyToken").asText();
            String theaterName = event.get("message").get("text").asText();

            Message responseMessage;

            List<Theater> theaters = theaterRepository.findTheatersByExactName(theaterName);

            if (theaters.isEmpty())
                theaters = theaterRepository.findTheatersByName(theaterName);

            if (theaters.isEmpty()) {
                responseMessage = new TextMessage("Sorry, I don't know where that theater is.");
            } else {
                if (theaters.size() > 1) {
                    List<Action> actions = theaters.stream().map(t -> new MessageAction(t.getName(), t.getName())).collect(Collectors.toList());
                    ConfirmTemplate template = new ConfirmTemplate( "More than one theaters found. Choose one.", actions);
                    responseMessage = new TemplateMessage("alttext", template);
                } else {
                    List<TheaterMovie> moviesInTheater = this.theaterMovieRepository.findMoviesScheduleInTheaterById(theaters.get(0).getId());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Showing movies schedule in " + theaters.get(0).getName() + "\n\n");
                    for (int i = 0; i < moviesInTheater.size(); i++) {
                        if (i > 0) stringBuilder.append("\n\n");
                        stringBuilder.append(moviesInTheater.get(i).getMovie().getTitle() + "\n");
                        stringBuilder.append("Plays at: " + StringUtils.join(moviesInTheater.get(i).getShowTimes(), ", "));
                    }

                    responseMessage = new TextMessage(stringBuilder.toString());
                }
            }

            try {
                LineMessagingClient client = new LineMessagingClientImpl(LineMessagingServiceBuilder.create(this.lineChannelToken).build());
                BotApiResponse response = client.replyMessage(new ReplyMessage(replyToken, responseMessage)).get();

                return ok(response.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ok("nothing");
    }

}
