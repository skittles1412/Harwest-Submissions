#include "bits/extc++.h"

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

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int n;
	cin >> n;
	vector<pair<long, long>> arr(n);
	long ans = 0;
	for(int i = 0; i < n; i++) {
		cin >> arr[i].first >> arr[i].second;
		ans += arr[i].second;
	}
	sort(begin(arr), end(arr));

	auto cmp = [&](const pair<int, long> &a, const pair<int, long> &b) {
		return a.second > b.second;
	};
	priority_queue<pair<int, long>, vector<pair<int, long>>, decltype(cmp)> pq(cmp);
	long dist[n];
	fill(dist, dist + n, LONG_MAX);

	auto put = [&](int u, long d) {
		if(d < dist[u]) {
			dbg(u, dist[u]);
			dist[u] = d;
			pq.emplace(u, d);
		}
	};
	put(0, 0);

	while(sz(pq)) {
		auto[u, d] = pq.top();
		pq.pop();
		dbg(u, d);
		if(dist[u] == d) {
			if(u > 0) {
				put(u - 1, d);
			}
			pair<long, long> p(arr[u].first + arr[u].second, INT_MAX);
			int ind = upper_bound(begin(arr), end(arr), p) - arr.begin();
			dbg(u, ind);
			if(ind < n) {
				assert(arr[ind].first - arr[u].first - arr[u].second >= 0);
				put(ind, d + arr[ind].first - arr[u].first - arr[u].second);
			}
			if(ind > 0) {
				put(ind - 1, d);
			}
		}
	}
	assert(dist[n - 1] < LONG_MAX);
	cout << ans + dist[n - 1] << endl;
}
