#include "bits/stdc++.h"

using namespace std;

//sad; long long double doesn't exist
using ld = long double;

//imagine a language where int = long
#define long long long

//typing too hard
#define endl "\n"

#define sz(x) int(x.size())

const long mod = 998244353;

long bpow(long a, long b) {
	long res = 1, cur = a;
	while(b) {
		if(b & 1) {
			res = (res * cur) % mod;
		}
		cur = (cur * cur) % mod;
		b /= 2;
	}
	return res;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int n, m, k;
	cin >> n >> m >> k;
	if(n == 1 || m == 1) {
		if(n == 1) {
			swap(n, m);
		}
		long ans = 0;
		for(int i = 0; i < k; i ++) {
			ans += bpow(i + 1, n) + mod - bpow(i, n);
		}
		cout << ans%mod << endl;
		return 0;
	}
	long ans = 0;
	for(int i = 0; i < k; i ++) {
		ans += (bpow(i + 1, n) + mod - bpow(i, n)) * bpow(k - i, m) % mod;
	}
	cout << ans % mod << endl;
}