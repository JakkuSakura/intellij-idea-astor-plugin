package org.bytecamp.program_repair.astor.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.28.1)",
    comments = "Source: astor_server.proto")
public final class AstorLanguageServerGrpc {

  private AstorLanguageServerGrpc() {}

  public static final String SERVICE_NAME = "routeguide.AstorLanguageServer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.bytecamp.program_repair.astor.grpc.ExecuteRequest,
      org.bytecamp.program_repair.astor.grpc.ExecuteResponse> getExecuteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Execute",
      requestType = org.bytecamp.program_repair.astor.grpc.ExecuteRequest.class,
      responseType = org.bytecamp.program_repair.astor.grpc.ExecuteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<org.bytecamp.program_repair.astor.grpc.ExecuteRequest,
      org.bytecamp.program_repair.astor.grpc.ExecuteResponse> getExecuteMethod() {
    io.grpc.MethodDescriptor<org.bytecamp.program_repair.astor.grpc.ExecuteRequest, org.bytecamp.program_repair.astor.grpc.ExecuteResponse> getExecuteMethod;
    if ((getExecuteMethod = AstorLanguageServerGrpc.getExecuteMethod) == null) {
      synchronized (AstorLanguageServerGrpc.class) {
        if ((getExecuteMethod = AstorLanguageServerGrpc.getExecuteMethod) == null) {
          AstorLanguageServerGrpc.getExecuteMethod = getExecuteMethod =
              io.grpc.MethodDescriptor.<org.bytecamp.program_repair.astor.grpc.ExecuteRequest, org.bytecamp.program_repair.astor.grpc.ExecuteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Execute"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.bytecamp.program_repair.astor.grpc.ExecuteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.bytecamp.program_repair.astor.grpc.ExecuteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AstorLanguageServerMethodDescriptorSupplier("Execute"))
              .build();
        }
      }
    }
    return getExecuteMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AstorLanguageServerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AstorLanguageServerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AstorLanguageServerStub>() {
        @java.lang.Override
        public AstorLanguageServerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AstorLanguageServerStub(channel, callOptions);
        }
      };
    return AstorLanguageServerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AstorLanguageServerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AstorLanguageServerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AstorLanguageServerBlockingStub>() {
        @java.lang.Override
        public AstorLanguageServerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AstorLanguageServerBlockingStub(channel, callOptions);
        }
      };
    return AstorLanguageServerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AstorLanguageServerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AstorLanguageServerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AstorLanguageServerFutureStub>() {
        @java.lang.Override
        public AstorLanguageServerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AstorLanguageServerFutureStub(channel, callOptions);
        }
      };
    return AstorLanguageServerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class AstorLanguageServerImplBase implements io.grpc.BindableService {

    /**
     */
    public void execute(org.bytecamp.program_repair.astor.grpc.ExecuteRequest request,
        io.grpc.stub.StreamObserver<org.bytecamp.program_repair.astor.grpc.ExecuteResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getExecuteMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getExecuteMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                org.bytecamp.program_repair.astor.grpc.ExecuteRequest,
                org.bytecamp.program_repair.astor.grpc.ExecuteResponse>(
                  this, METHODID_EXECUTE)))
          .build();
    }
  }

  /**
   */
  public static final class AstorLanguageServerStub extends io.grpc.stub.AbstractAsyncStub<AstorLanguageServerStub> {
    private AstorLanguageServerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AstorLanguageServerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AstorLanguageServerStub(channel, callOptions);
    }

    /**
     */
    public void execute(org.bytecamp.program_repair.astor.grpc.ExecuteRequest request,
        io.grpc.stub.StreamObserver<org.bytecamp.program_repair.astor.grpc.ExecuteResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getExecuteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class AstorLanguageServerBlockingStub extends io.grpc.stub.AbstractBlockingStub<AstorLanguageServerBlockingStub> {
    private AstorLanguageServerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AstorLanguageServerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AstorLanguageServerBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<org.bytecamp.program_repair.astor.grpc.ExecuteResponse> execute(
        org.bytecamp.program_repair.astor.grpc.ExecuteRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getExecuteMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class AstorLanguageServerFutureStub extends io.grpc.stub.AbstractFutureStub<AstorLanguageServerFutureStub> {
    private AstorLanguageServerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AstorLanguageServerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AstorLanguageServerFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_EXECUTE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AstorLanguageServerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AstorLanguageServerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_EXECUTE:
          serviceImpl.execute((org.bytecamp.program_repair.astor.grpc.ExecuteRequest) request,
              (io.grpc.stub.StreamObserver<org.bytecamp.program_repair.astor.grpc.ExecuteResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class AstorLanguageServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AstorLanguageServerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.bytecamp.program_repair.astor.grpc.AstorLanguageServerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AstorLanguageServer");
    }
  }

  private static final class AstorLanguageServerFileDescriptorSupplier
      extends AstorLanguageServerBaseDescriptorSupplier {
    AstorLanguageServerFileDescriptorSupplier() {}
  }

  private static final class AstorLanguageServerMethodDescriptorSupplier
      extends AstorLanguageServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AstorLanguageServerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AstorLanguageServerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AstorLanguageServerFileDescriptorSupplier())
              .addMethod(getExecuteMethod())
              .build();
        }
      }
    }
    return result;
  }
}
