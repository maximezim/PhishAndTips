import { createLogger, format, transports } from 'winston';

// Création du logger de base
const baseLogger = createLogger({
	format: format.combine(
		format.colorize(),
		format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
		format.printf(({ timestamp, level, message, service }) => {
			const blue = '\x1b[34m';
			const reset = '\x1b[0m';
			const serviceLabel = service ? `${blue}[${service}]${reset} ` : '';
			return `${serviceLabel}${timestamp} [${level}]: ${message}`;
		})
	),
	transports: [new transports.Console()]
});

export const getServiceLogger = (serviceName: string) => {
	return baseLogger.child({ service: serviceName });
};

export default baseLogger;
