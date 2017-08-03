package prime;

import java.util.ArrayList;

public class primeImp implements Prime {
	@Override
	public ArrayList<Long> comutePrimes(int n){
		ArrayList<Long> list = new ArrayList<Long>();
		int counter = 0;
		long number = 2;
		while(counter<n){
			if(isPrime(number)){
				list.add(number);
				counter++;
			}
			number++;
		}
		return list;
	}

	public boolean isPrime(long num) {
		long n = num;
        boolean isPrime = true;
        if (n < 2) isPrime = false;

        // try all possible factors of n
        // if if n has a factor, then it has one less than or equal to sqrt(n),
        // so for efficiency we only need to check factor <= sqrt(n) or
        // equivalently factor*factor <= n
        for (long factor = 2; factor*factor <= n; factor++) {

            // if factor divides evenly into n, n is not prime, so break out of loop
            if (n % factor == 0) {
                isPrime = false;
                break;
            }
        }
        return isPrime;
	}
}
