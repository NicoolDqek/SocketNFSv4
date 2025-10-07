#include <stdio.h>
#include <mpi.h>

int main(int argc, char *argv[]) {
    int rank, size;
    long long i, start, end, N = 9000000;
    double local = 0.0, total = 0.0;
    double t1, t2;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (rank == 0) t1 = MPI_Wtime();

    start = rank * (N / size);
    end = (rank + 1) * (N / size);

    for (i = start; i < end; i++) {
        if (i % 2 == 0) local += 1.0 / (2.0 * i + 1.0);
    }

    MPI_Reduce(&local, &total, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        t2 = MPI_Wtime();
        printf("PI ≈ %.10f\n", 4.0 * total);
        printf("Tiempo: %.6f s con %d procesos\n", t2 - t1, size);
    }

    MPI_Finalize();
    return 0;
}
