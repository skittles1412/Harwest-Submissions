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

void solve() {
	int n, l, r;
	cin >> n >> l >> r;
	int arr[n];
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
		arr[i]--;
	}
	int cnt[n][2] {};
	for(int i = 0; i < l; i++) {
		cnt[arr[i]][0]++;
	}
	for(int i = l; i < n; i++) {
		cnt[arr[i]][1]++;
	}
	int cl = 0, cr = 0;
	long ans = 0;
	for(int i = 0; i < n; i++) {
		int sub = min(cnt[i][0], cnt[i][1]);
		cnt[i][0] -= sub;
		cnt[i][1] -= sub;
		cl += cnt[i][0];
		cr += cnt[i][1];
	}
	int diff = abs(cr - cl);
	assert(diff % 2 == 0);
	diff /= 2;
	ans += diff;
	int tot = 0;
	for(int i = 0; i < n; i++) {
		if(cr < cl) {
			int sub = min(diff, cnt[i][0] / 2);
			cnt[i][0] -= sub * 2;
			diff -= sub;
		}else {
			int sub = min(diff, cnt[i][1] / 2);
			cnt[i][1] -= sub * 2;
			diff -= sub;
		}
		tot += cnt[i][0] + cnt[i][1];
	}
	assert(tot % 2 == 0);
	ans += tot / 2;
	cout << ans << endl;
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
//		cout << "Case #" << _ << ": ";
		solve();
	}
}
