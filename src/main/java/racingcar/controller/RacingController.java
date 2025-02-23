package racingcar.controller;

import java.util.List;
import java.util.stream.Collectors;

import racingcar.domain.entity.Attempt;
import racingcar.domain.entity.car.Car;
import racingcar.domain.entity.car.CarNames;
import racingcar.domain.entity.Race;
import racingcar.domain.vo.CarNameVO;
import racingcar.domain.vo.CarVO;
import racingcar.view.InputView;
import racingcar.view.OutputMessage;
import racingcar.view.OutputView;

public class RacingController {
	private final InputView inputView;
	private final OutputView outputView;

	public RacingController(InputView inputView, OutputView outputView) {
		this.inputView = inputView;
		this.outputView = outputView;
	}

	public void run() {
		CarNames carNames = inputCarName();
		Attempt attempt = inputAttempt();
		List<Car> cars = registerCars(carNames);
		Race race = Race.from(attempt, cars);
		printRaceResult(race);
		List<CarNameVO> winnerList = getWinnerList(race);
		printWinner(winnerList);
	}

	private CarNames inputCarName() {
		outputView.print(OutputMessage.INPUT_CAR_NAME);
		String input = inputView.readLine();
		return CarNames.from(input);
	}

	private Attempt inputAttempt() {
		outputView.print(OutputMessage.INSERT_ATTEMPT);
		String input = inputView.readLine();
		return Attempt.from(input);
	}

	private List<Car> registerCars(CarNames carNames) {
		return carNames.toCars();
	}

	private void printRaceResult(Race race) {
		outputView.print(OutputMessage.RESULT_MESSAGE);
		while (race.canProgress()) {
			String[][] resultSentence = race.createResultSentence();
			printRaceResultSentence(resultSentence);
			outputView.lineFeed();
		}
	}

	private void printRaceResultSentence(String[][] resultSentence) {
		for (String[] sentence : resultSentence) {
			outputView.print(OutputMessage.DISTANCE, sentence[0], sentence[1]);
		}
	}

	private List<CarNameVO> getWinnerList(Race race) {
		List<CarVO> carVOList = race.toCarVOList();
		return CarVO.getWinnerList(carVOList);
	}

	private void printWinner(List<CarNameVO> winnerList) {
		String result = winnerList.stream()
			.map(CarNameVO::value)
			.collect(Collectors.joining(", "));
		outputView.print(OutputMessage.WINNER, result);
	}
}
