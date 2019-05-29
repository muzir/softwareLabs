import java.util.concurrent.RecursiveAction;

public class ArraySum extends RecursiveAction {

	int[] input;
	int startIndex;
	int endIndex;
	int sum;

	public ArraySum(int startIndex, int mid, int[] input) {
		this.startIndex = startIndex;
		this.endIndex = mid;
		this.input = input;
	}

	@Override
	protected void compute() {
		if (startIndex == endIndex)
			sum = input[startIndex];
		else if (startIndex > endIndex)
			sum = 0;
		else {
			int mid = (startIndex + endIndex) / 2;
			ArraySum left = new ArraySum(startIndex, mid, input);
			ArraySum right = new ArraySum(mid + 1, endIndex, input);
			left.fork();
			right.compute();
			left.join();
			sum = left.sum + right.sum;
		}
	}
}
