#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

const int maxn = 1e5;
vector<int> primes;

void pcomp() {
	bool prime[maxn + 1] {};
	memset(prime, 1, sizeof(prime));
	for(int i = 2; i <= maxn; i++) {
		if(prime[i]) {
			primes.push_back(i);
			if(maxn / i > i) {
				for(int j = i * i; j <= maxn; j += i) {
					prime[j] = false;
				}
			}
		}
	}
}

void solve() {
	int n;
	cin >> n;
	vector<int> arr(n), pos(n);
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
		arr[i]--;
		pos[arr[i]] = i;
	}
	vector<pair<int, int>> swaps;
	auto sw = [&](int a, int b) {
		swap(pos[arr[a]], pos[arr[b]]);
		swap(arr[a], arr[b]);
		swaps.emplace_back(a, b);
	};
	for(int i = 0; i < n; i++) {
		while(pos[i] != i) {
			int diff = abs(i - pos[i]) + 1;
			auto it = upper_bound(begin(primes), end(primes), diff);
			assert(it != primes.begin());
			it--;
			int v = *it - 1;
			if(pos[i] < i) {
				sw(pos[i], pos[i] + v);
			}else {
				sw(pos[i], pos[i] - v);
			}
		}
	}
	cout << sz(swaps) << endl;
	for(auto &[a, b]: swaps) {
		if(a > b) {
			swap(a, b);
		}
		cout << a + 1 << " " << b + 1 << endl;
	}
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	pcomp();
	int t = 1;
//	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		solve();
	}
}
