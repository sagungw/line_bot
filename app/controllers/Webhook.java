package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.*;
import com.linecorp.bot.model.response.*;

import com.linecorp.bot.client.*;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import retrofit2.Response;


public class Webhook extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result webhook(){
        JsonNode root = request().body().asJson();

        JsonNode events = root.get("events");

        for(final JsonNode event : events) {
            String replyToken = event.get("replyToken").asText();

            TextMessage textMessage = new TextMessage("helllo test ajaaa");
            ReplyMessage replyMessage = new ReplyMessage(
                    replyToken,
                    textMessage
            );

            try {
                Response<BotApiResponse> response =
                        LineMessagingServiceBuilder
                                .create("rP63jrXrKhxQRsiHW4y/kvJxajyOXeAiC3/mR0rziwHlo34mrKpNyRZY0xlA5Py9KfDkZhhWEnRTDsNDpBGE5d/JPsAtCCgLzxnSWQxoHoRHQr6xSB7nxS2jY9r92G/abL2IO8W/UbFeaAoBK1SczgdB04t89/1O/w1cDnyilFU=")
                                .build()
                                .replyMessage(replyMessage)
                                .execute();

                return ok(response.code() + " "+response.message());


            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return ok("nothign");
    }

}
