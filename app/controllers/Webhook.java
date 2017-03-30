package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineMessagingClientImpl;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import models.Theater;
import models.TheaterMovie;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
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

    private final static String NON_THIN = "[^iIl1\\.,']";

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

            List<Message> responses = new ArrayList<>();

            List<Theater> theaters = theaterRepository.findTheatersByExactName(theaterName);

            if (theaters.isEmpty())
                theaters = theaterRepository.findTheatersByName(theaterName);

            if (theaters.isEmpty()) {
                responses.add(new TextMessage("Maaf, aku tidak tahu theater yang kamu maksud. "));
            } else {
                if (theaters.size() > 1) {
                    StringBuilder string = new StringBuilder("\n\n");

                    List<Action> actions = new ArrayList<>();

                    for (int i = 0; i < Math.min(theaters.size(), 4); i++) {
                        int counter = i + 1;

                        Theater t = theaters.get(i);

                        string.append(counter + ". " + t.getName() + "\n");

                        String name = t.getName();
                        if (name.length() >= 16) {
                            name = ellipsize(name, 16);
                        }

                        MessageAction messageAction = new MessageAction(counter + ". " + name, t.getName());
                        actions.add(messageAction);
                    }

                    responses.add(new TextMessage("Aku menemukan beberapa theater" + string.toString()));

                    ButtonsTemplate template = new ButtonsTemplate(null, "Options", "Silahkan pilih salah satu", actions);
                    responses.add(new TemplateMessage("alttext", template));

                } else {
                    List<TheaterMovie> moviesInTheater = this.theaterMovieRepository.findMoviesScheduleInTheaterById(theaters.get(0).getId());

                    if (moviesInTheater.size() > 0) {

                        responses.add(new TextMessage("Berikut film2 yang lagi tayang"));

                        for (int i = 0; i < Math.max(1, Math.ceil((float) moviesInTheater.size() / 5)); i++) {
                            List<CarouselColumn> columns = new ArrayList<>();

                            for (int j = i * 5; j < Math.min(moviesInTheater.size(), (i+1) * 5); j++) {
                                TheaterMovie theaterMovie = moviesInTheater.get(j);

                                List<Action> actions = new ArrayList<>();
                                actions.add(new URIAction("Lihat Detail", theaterMovie.getMovie().getMovieUrl()));

                                String text = "Plays at : " + StringUtils.join(theaterMovie.getShowTimes(), ", ");
                                if (text.length() > 60) {
                                    text = text.substring(0, 60);
                                }

                                CarouselColumn column = new CarouselColumn(null, theaterMovie.getMovie().getTitle(), text, actions);
                                columns.add(column);
                            }

                            responses.add(new TemplateMessage("List Film " + (i + 1), new CarouselTemplate(columns)));
                        }

                    } else {
                        responses.add(new TextMessage("Maaf saat ini tidak ada film yang tersedia."));
                    }
                }
            }

            try {
                LineMessagingClient client = new LineMessagingClientImpl(LineMessagingServiceBuilder.create(this.lineChannelToken).build());
                BotApiResponse response = client.replyMessage(new ReplyMessage(replyToken, responses)).get();

                return ok(response.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ok("nothing");
    }

    public static String ellipsize(String text, int max) {

        if (textWidth(text) <= max)
            return text;

        // Start by chopping off at the word before max
        // This is an over-approximation due to thin-characters...
        int end = text.lastIndexOf(' ', max - 3);

        // Just one long word. Chop it off.
        if (end == -1)
            return text.substring(0, max - 3) + "...";

        // Step forward as long as textWidth allows.
        int newEnd = end;
        do {
            end = newEnd;
            newEnd = text.indexOf(' ', end + 1);

            // No more spaces.
            if (newEnd == -1)
                newEnd = text.length();

        } while (textWidth(text.substring(0, newEnd) + "...") < max);

        return text.substring(0, end) + "...";
    }

    private static int textWidth(String str) {
        return (int) (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
    }
}
