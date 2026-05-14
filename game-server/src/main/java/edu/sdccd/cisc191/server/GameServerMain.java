package edu.sdccd.cisc191.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GameServerMain {

    private static final int PORT = 50051;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(PORT)
                .addService(new GameServiceImpl())
                .build();

        server.start();

        System.out.println("1v1 gRPC Game Server started on port " + PORT);
        System.out.println("Press Ctrl+C to stop.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping gRPC Game Server...");
            server.shutdown();
        }));

        server.awaitTermination();
    }
}

/*
* What is the purpose of FXML in this project?
*   The purpose of the FXML file is to design how the javafx looks. It's where you
*   set up the scene, add the labels, rig the buttons, etc.
* What is the controller responsible for?
*   The controller is responsible for controlling the app. It's what handles every interaction
*   of the user (events) with the app and updates the screen accordingly. It also calls the model
*   or service.
* What is the model responsible for?
*   The model serves as the main way of managing the data and much of the logic in the program.
* What is the gRPC server responsible for?
*   The grpc is what connects the JavaFx client to the game server. It provides the data about
*   the current matches.
* Why should JavaFX network calls run inside a Task instead of directly in the button handler?
*   It's best to keep the UI and logic separated in different threads. Having a separate call
*   lets the UI continue working and responding while it waits for a response.
* What changed in the .proto file?
*   The JoinMatchResponse inside the .proto file now provides a match summary.
* Why do both the client and server need matching .proto files?
*   Sharing the .proto file ensures that the client and the server communicate properly.
* What does Maven regenerate after a .proto change?
*   After a .proto change, Maven regenerates the Java protobuf and gRPC source classes from
*   the updated protocol definitions so both the client and server can use the
*   new messages and services.
* What shared state exists in this lab?
*   The joined match count and completed match count are shared state because multiple
*   threads or gRPC requests can access and modify them at the same time.
* Why is count++ not thread-safe?
*   Because count++ is actually multiple operations: read the value, add 1,
*   and write it back. If two threads do this at the same time, one update can
*   overwrite the other, causing incorrect results.
* How does AtomicInteger help with thread safety?
*   Atomic integer ensures the number is read, modified, and saved in a single action. This
*   ensures the correct information gets stored and saved.
* Why might a gRPC server need thread-safe shared data structures?
*   Because it needs to avoid being in an inconsistent state where the values are being
*   modified by multiple different calls simultaneously.
* Why should JavaFX controls be updated on the JavaFX Application Thread?
*   JavaFX controls are not thread-safe, so they must only be updated on the JavaFX
*   Application Thread. Updating them from background threads can cause crashes,
*   inconsistent behavior, or runtime exceptions. Using Platform.runLater() ensures the
*   updates happen safely on the correct thread.
* Which unit test helped you the most, and why?
*   For me, the most helpful test was buildJoinLogMessageFormatsRankedMatch, this is
*   because I was writing the sentence I was returning incorrectly and the test
*   helped me realize that.
* How did you complete the FXML TODO?
*   I added the summary label to the file.
* How did you complete the MVC/model TODO?
*   I built a method to make short match summaries. I also made the match count an atomic
*   integer.
* How did you complete the controller TODO?
*   I built a method to make join log message. I changed handleJoinMatch to implement the
*   join method I built. I also  also completed a helper so UI updates were safe from any
*   thread.
* How did you complete the gRPC client TODO?
*   For the grpc client, first I implemented the method to build match requests. This  method trims
*   the names and gives default ones if none are chosen. Then I implemented this method in
*   joinMatchTask. I also added the summary parameter in its corresponding .proto file.
* How did you complete the gRPC server TODO?
*   I added the summary parameter in its corresponding .proto file. I added the summary to joinMatch and
*   I made match statistics thread-safe.
* How did you make the model completed-match counter thread-safe?
*   I used atomic integers to ensure the numbers increment only once on every call and don't
*   get overridden.
* How did you make JavaFX UI updates safe from background threads?
*   I  used Platform.runLater() and the runOnFxThread() helper methods,to ensure that the UI update calls
*   don't all happen at the same time.
* How did you make server statistics safe for concurrent gRPC requests?
*   I used atomic integers to ensure they are incremented once every time the method is called.
*/
