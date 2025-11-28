package be.kdg.player.adapter.in.request;

public record RegisterPlayerRequest( String username,
                                     String email,
                                     String password,
                                     String pictureUrl) {
}
