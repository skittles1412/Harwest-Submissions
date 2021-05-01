#include "bits/extc++.h"

using namespace std;

template<class T, class U = less<T>>
using rt = __gnu_pbds::tree<T, __gnu_pbds::null_type, U, __gnu_pbds::rb_tree_tag, __gnu_pbds::tree_order_statistics_node_update>;

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

const long mod = 1e9 + 7;

/**
 * dp[i][j] = ans to i...n, current val can be at most j
 */

void solve() {
	int n;
	cin >> n;
	int arr[n + 1];
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
		arr[i]--;
	}
	arr[n] = INT_MAX;
	long ans = 0;
	long dp[n + 1] {};
	for(int i = n - 1; i >= 0; i--) {
		int j = i == 0 ? INT_MAX : arr[i - 1];

		ans += (dp[i + 1] * min(arr[i], arr[i + 1]) + arr[i]) % mod;

		dp[i] = (dp[i + 1] * min({arr[i], arr[i + 1], j}) + min(j, arr[i])) % mod;
		dbg(i, dp[i]);
	}
	cout << ans % mod << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int t = 1;
//	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		dbg(_);
//		cout << "Case #" << _ << ": ";
		solve();
	}
}
