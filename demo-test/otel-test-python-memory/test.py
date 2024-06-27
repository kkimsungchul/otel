from heapsy import HeapDendrogram


if __name__ == '__main__':
    dendrogram = HeapDendrogram()
    dendrogram.generate()

    print(dendrogram.total_objects())

    size = sum([d.size for d in dendrogram.decendants()])
    print(size / 1024, 'KB')

    count = sum([1 for d in dendrogram.decendants()])
    print(count)
    p = dendrogram.as_prometheus_metric()
    print(p)