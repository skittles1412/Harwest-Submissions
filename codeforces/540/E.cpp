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
	int n;
	cin >> n;
	map<int, int> m;
	auto get = [&](int x) -> int & {
		auto &ret = m[x];
		if(ret == 0) {
			ret = x;
		}
		return ret;
	};
	for(int i = 0; i < n; i++) {
		int u, v;
		cin >> u >> v;
		swap(get(u), get(v));
	}
	rt<int> t;
	long ans = 0;
	vector<pair<int, int>> arr(begin(m), end(m));
	n = sz(arr);
	for(int i = n - 1; i >= 0; i--) {
		auto &[ind, x] = arr[i];
		dbg(i, ind, x);
		if(ind < x) {
			ans += x - ind;
			ans -= upper_bound(begin(arr), end(arr), make_pair(x, INT_MAX)) - (arr.begin() + i + 1);
			dbg(i, ans);
		}else if(x < ind) {
			ans += ind - x;
			ans -= (arr.begin() + i) - lower_bound(begin(arr), end(arr), make_pair(x, 0));
			dbg(i, ans);
		}
		ans += t.order_of_key(x);
		t.insert(x);
		dbg(i, ans);
	}
	cout << ans << endl;
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
