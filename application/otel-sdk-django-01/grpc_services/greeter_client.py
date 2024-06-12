import grpc
from pb import example_pb2
from pb import example_pb2_grpc
def run():
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = example_pb2_grpc.GreeterStub(channel)
        response = stub.SayHello(example_pb2.HelloRequest(name='Django User'))
        print("Greeter client received: " + response.message)

if __name__ == '__main__':
    run()