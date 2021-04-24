#include "bits/stdc++.h"

using namespace std;

template<class T, class U>
void sep(T &out, const string &s, const U &u) {
	out << u;
}

template<class T, class Head, class ...Tail>
void sep(T &out, const string &s, const Head &h, const Tail &...t) {
	out << h << s;
	sep(out, s, t...);
}

#ifdef DEBUG
#define dbg(...)                                                      \
cerr << "L" << __LINE__ << " [" << #__VA_ARGS__ << "]" << ": ";       \
sep(cerr, " | ", __VA_ARGS__);                                        \
cerr << endl

#else
#define cerr if(false) cerr
#define dbg(...) cerr
#endif

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

const int maxn = 2e5;
const long mod = 998244353;

void slow(int n, vector<long> arr) {
	int ans = 0;
	for(int i = 0; i < (1 << n); i++) {
		vector<int> a, b;
		long as = 0, bs = 0;
		for(int j = 0; j < n; j++) {
			if((i >> j) & 1) {
				a.push_back(j);
			}else {
				b.push_back(j);
			}
		}
		for(int j = 1; j < sz(a) - 1; j++) {
			if(!(a[j] - a[j - 1] <= a[j + 1] - a[j])) {
				goto loop;
			}
		}
		for(int j = 1; j < sz(b) - 1; j++) {
			if(!(b[j] - b[j - 1] >= b[j + 1] - b[j])) {
				goto loop;
			}
		}
		for(auto &x: a) {
			as += arr[x];
		}
		for(auto &x: b) {
			bs += arr[x];
		}
		if(as < bs) {
			ans++;
		}
		loop:;
	}
	cout << ans << endl;
}

long solve(vector<long> arr, long as, long bs) {
	int n = sz(arr);
	long psum[n + 1];
	for(int i = 0; i < n; i++) {
		psum[i + 1] = psum[i] + arr[i];
	}
	vector<long> ppsum[2] {vector<long>(n + 1), vector<long>(n + 1)};
	for(int i = 0; i < n; i++) {
		for(auto &j: ppsum) {
			j[i + 1] = j[i];
		}
		ppsum[i & 1][i + 1] += arr[i];
	}
	auto sum = [&](int l, int r) {
		return psum[r + 1] - psum[l];
	};
	auto osum = [&](int p, int l, int r) {
		return ppsum[p][r + 1] - ppsum[p][l];
	};
	auto valid = [&](int l, int r) {
		assert(((r - l + 1) & 1) == 0);
		long ax = as, bx = bs;
		ax += sum(0, l - 1);
		bx += sum(r + 1, n - 1);
		ax += osum((l + 1) & 1, l, r);
		bx += osum((r - 1) & 1, l, r);
		if(ax < bx) {
//			dbg(l, r);
//			dbg(ax, bx);
			return true;
		}
		return ax < bx;
	};
	long ans = 0;
	for(int i = 1; i < n; i++) {
		int add = (i - 1) & 1;
		int start = (i - 1) / 2;
		int l = start - 1, r = (n - 1) / 2;
		if(r * 2 + add >= n - 1) {
			r--;
		}
		while(l < r) {
			int mid = (l + r + 1) / 2;
			assert(start - 1 != mid);
			if(valid(i, mid * 2 + add)) {
				l = mid;
			}else {
				r = mid - 1;
			}
		}
		ans += l - start + 1;
	}
	return ans;
}

void solve() {
	int n;
	cin >> n;
	vector<long> arr(n);
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
	}
	if(n <= 4) {
		slow(n, arr);
		return;
	}
	long ans = 0;

	long tsum = accumulate(begin(arr), end(arr), 0LL);
	long psum[n + 1];
	psum[0] = 0;
	for(int i = 0; i < n; i++) {
		psum[i + 1] = psum[i] + arr[i];
	}

//	dbg(tsum);
	for(int i = 0; i <= n; i++) {
		if(tsum - psum[i] < psum[i]) {
			ans++;
//			dbg(i, tsum - psum[i], psum[i]);
		}
	}

	auto subs = [&](int l, int r) {
		return vector<long>(arr.begin() + l, arr.begin() + r);
	};

//	dbg(ans);
	ans += solve(subs(0, n - 1), arr[n - 1], 0);
//	dbg("CC", ans);

	ans += solve(subs(0, n), 0, 0);
//	dbg("CP", ans);

	ans += solve(subs(1, n - 1), arr[n - 1], arr[0]);
//	dbg("PC", ans);

	ans += solve(subs(1, n), 0, arr[0]);
//	dbg("PP", ans);

	cout << ans % mod << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int t = 1;
	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		dbg(_);
		solve();
	}
}
