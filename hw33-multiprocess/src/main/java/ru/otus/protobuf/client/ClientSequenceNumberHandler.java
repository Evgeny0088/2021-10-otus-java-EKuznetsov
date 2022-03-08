package ru.otus.protobuf.client;

import java.util.function.Consumer;

public interface ClientSequenceNumberHandler<ServerNumber> extends Consumer<ServerNumber> {

}
