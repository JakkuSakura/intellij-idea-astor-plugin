package org.bytecamp.program_repair.backend.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.28.1)",
    comments = "Source: service.proto")
public final class RepairServerGrpc {

  private RepairServerGrpc() {}

  public static final String SERVICE_NAME = "RepairServer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<RepairTaskRequest,
      RepairTaskResponse> getSubmitTaskMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubmitTask",
      requestType = RepairTaskRequest.class,
      responseType = RepairTaskResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<RepairTaskRequest,
      RepairTaskResponse> getSubmitTaskMethod() {
    io.grpc.MethodDescriptor<RepairTaskRequest, RepairTaskResponse> getSubmitTaskMethod;
    if ((getSubmitTaskMethod = RepairServerGrpc.getSubmitTaskMethod) == null) {
      synchronized (RepairServerGrpc.class) {
        if ((getSubmitTaskMethod = RepairServerGrpc.getSubmitTaskMethod) == null) {
          RepairServerGrpc.getSubmitTaskMethod = getSubmitTaskMethod =
              io.grpc.MethodDescriptor.<RepairTaskRequest, RepairTaskResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SubmitTask"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RepairTaskRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RepairTaskResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RepairServerMethodDescriptorSupplier("SubmitTask"))
              .build();
        }
      }
    }
    return getSubmitTaskMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RepairServerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RepairServerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RepairServerStub>() {
        @Override
        public RepairServerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RepairServerStub(channel, callOptions);
        }
      };
    return RepairServerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RepairServerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RepairServerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RepairServerBlockingStub>() {
        @Override
        public RepairServerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RepairServerBlockingStub(channel, callOptions);
        }
      };
    return RepairServerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RepairServerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RepairServerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RepairServerFutureStub>() {
        @Override
        public RepairServerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RepairServerFutureStub(channel, callOptions);
        }
      };
    return RepairServerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class RepairServerImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<RepairTaskRequest> submitTask(
        io.grpc.stub.StreamObserver<RepairTaskResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getSubmitTaskMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSubmitTaskMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                RepairTaskRequest,
                RepairTaskResponse>(
                  this, METHODID_SUBMIT_TASK)))
          .build();
    }
  }

  /**
   */
  public static final class RepairServerStub extends io.grpc.stub.AbstractAsyncStub<RepairServerStub> {
    private RepairServerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RepairServerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RepairServerStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<RepairTaskRequest> submitTask(
        io.grpc.stub.StreamObserver<RepairTaskResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getSubmitTaskMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class RepairServerBlockingStub extends io.grpc.stub.AbstractBlockingStub<RepairServerBlockingStub> {
    private RepairServerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RepairServerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RepairServerBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class RepairServerFutureStub extends io.grpc.stub.AbstractFutureStub<RepairServerFutureStub> {
    private RepairServerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RepairServerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RepairServerFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SUBMIT_TASK = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RepairServerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RepairServerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBMIT_TASK:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.submitTask(
              (io.grpc.stub.StreamObserver<RepairTaskResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RepairServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RepairServerBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return RepairServerProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RepairServer");
    }
  }

  private static final class RepairServerFileDescriptorSupplier
      extends RepairServerBaseDescriptorSupplier {
    RepairServerFileDescriptorSupplier() {}
  }

  private static final class RepairServerMethodDescriptorSupplier
      extends RepairServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RepairServerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RepairServerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RepairServerFileDescriptorSupplier())
              .addMethod(getSubmitTaskMethod())
              .build();
        }
      }
    }
    return result;
  }
}
